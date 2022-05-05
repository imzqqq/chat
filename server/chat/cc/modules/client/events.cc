// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

using namespace ircd;

mapi::header
IRCD_MODULE
{
	"Client 14.17.1.1 :Room Previews"
};

static bool
append_event(json::stack::array &out,
             const m::event &event,
             const m::event::idx &event_idx,
             const int64_t &room_depth,
             const m::user::room &);

static bool
get_events_from(client &client,
                const m::resource::request &request,
                const m::room::id &room_id,
                const m::event::id &event_id,
                const m::event::id &room_head,
                const int64_t &room_depth,
                json::stack::object &out);

static resource::response
get__events(client &client,
            const m::resource::request &request);

m::resource
events_resource
{
	"/chat/client/r0/events",
	{
		"(14.17.1.1) Room Previews"
	}
};

conf::item<ircd::milliseconds>
timeout_max
{
	{ "name",     "ircd.client.events.timeout.max"  },
	{ "default",  15 * 1000L                        },
};

conf::item<ircd::milliseconds>
timeout_min
{
	{ "name",     "ircd.client.events.timeout.min"  },
	{ "default",  5 * 1000L                         },
};

ircd::conf::item<ircd::milliseconds>
timeout_default
{
	{ "name",     "ircd.client.events.timeout.default"  },
	{ "default",  10 * 1000L                            },
};

static conf::item<size_t>
events_limit
{
	{ "name",      "ircd.client.rooms.events.limit" },
	{ "default",   32L                              },
};

static conf::item<size_t>
buffer_size
{
	{ "name",     "ircd.client.rooms.events.buffer_size" },
	{ "default",  long(128_KiB)                          },
};

static conf::item<size_t>
flush_hiwat
{
	{ "name",     "ircd.client.rooms.events.flush.hiwat" },
	{ "default",  long(16_KiB)                           },
};

m::resource::method
method_get
{
	events_resource, "GET", get__events
};

struct waiter
{
	m::user::id user_id;
	m::room::id room_id;
	std::string *event;
	m::event::id::buf *event_id;
	ctx::dock *dock;
};

std::list<waiter>
clients;

resource::response
get__events(client &client,
            const m::resource::request &request)
{
	if(!request.query["room_id"])
		throw m::UNSUPPORTED
		{
			"Specify a room_id or use /sync"
		};

	m::room::id::buf room_id
	{
		url::decode(room_id, request.query.at("room_id"))
	};

	m::event::id::buf event_id
	{
		request.query["from"]?
			url::decode(event_id, request.query.at("from")):
			m::head(room_id)
	};

	const m::room room
	{
		room_id, event_id
	};

	if(!visible(room, request.user_id))
		throw m::ACCESS_DENIED
		{
			"You are not able to view the room at this event."
		};

	resource::response::chunked response
	{
		client, http::OK, buffer_size
	};

	json::stack out
	{
		response.buf, response.flusher(), size_t(flush_hiwat)
	};

	json::stack::object top
	{
		out
	};

	const auto &[room_head, room_depth, room_head_idx]
	{
		m::top(room_id)
	};

	json::stack::member
	{
		top, "start", event_id
	};

	if(event_id && event_id != room_head)
	{
		json::stack::checkpoint checkpoint
		{
			out
		};

		if(get_events_from(client, request, room_id, event_id, room_head, room_depth, top))
			return std::move(response);

		checkpoint.rollback();
	}

	ctx::dock dock;
	std::string event;
	m::event::id::buf eid;
	const unique_iterator it
	{
		clients, clients.emplace(end(clients), waiter{request.user_id, room_id, &event, &eid, &dock})
	};

	const milliseconds timeout
	{
		minmax
		(
			request.query.get("timeout", milliseconds(timeout_default)),
			milliseconds(timeout_min),
			milliseconds(timeout_max)
		)
	};

	dock.wait_for(timeout, [&event, &eid]
	{
		return !empty(event) && !empty(eid);
	});

	if(!event.empty())
	{
		const m::event &event_
		{
			json::object(event), eid
		};

		const auto &event_idx
		{
			event_.event_id?
				m::index(event_):
				0UL
		};

		const auto &room_depth
		{
			m::depth(room_id)
		};

		const m::user::room user_room
		{
			request.user_id
		};

		json::stack::array chunk
		{
			top, "chunk"
		};

		append_event(chunk, event_, event_idx, room_depth, user_room);
	}
	else json::stack::array
	{
		top, "chunk"
	};

	if(eid)
		json::stack::member
		{
			top, "end", eid
		};
	else
		json::stack::member
		{
			top, "end", room_head
		};

	return std::move(response);
}

static void
handle_notify(const m::event &,
              m::vm::eval &);

m::hookfn<m::vm::eval &>
notified
{
	handle_notify,
	{
		{ "_site",  "vm.notify" },
	}
};

void
handle_notify(const m::event &event,
              m::vm::eval &eval)
try
{
	const auto &room_id
	{
		json::get<"room_id"_>(event)
	};

	// Prevent EDU's from notifying
	if(!event.event_id || !room_id)
		return;

	for(auto &waiter : clients)
	{
		if(!waiter.event->empty())
			continue;

		if(waiter.room_id != room_id)
			continue;

		assert(waiter.event_id);
		*waiter.event_id = event.event_id?
			m::event::id::buf{event.event_id}:
			m::event::id::buf{};

		assert(waiter.event);
		*waiter.event = json::strung{event};

		assert(waiter.dock);
		waiter.dock->notify_one();
	}
}
catch(const std::exception &e)
{
	log::critical
	{
		m::log, "client/events vm.notify hook :%s",
		e.what()
	};
}

bool
get_events_from(client &client,
                const m::resource::request &request,
                const m::room::id &room_id,
                const m::event::id &event_id,
                const m::event::id &room_head,
                const int64_t &room_depth,
                json::stack::object &out)
{
	const m::user::room user_room
	{
		request.user_id
	};

	m::room::events it
	{
		room_id, event_id
	};

	if(!it)
		return false;

	json::stack::array chunk
	{
		out, "chunk"
	};

	size_t i(0), j(0);
	for(; it && i < size_t(events_limit); --it, ++i)
	{
		const m::event &event{*it};
		if(!visible(event, request.user_id))
			continue;

		const auto &event_idx(it.event_idx());
		j += append_event(chunk, event, event_idx, room_depth, user_room);
	}

	if(!j)
		return j;

	chunk.~array();
	const m::event::id::buf end_token
	{
		it?
			m::event_id(it.event_idx()):
			room_head
	};

	json::stack::member
	{
		out, "end", end_token
	};

	return j;
}

bool
append_event(json::stack::array &out,
             const m::event &event,
             const m::event::idx &event_idx,
             const int64_t &room_depth,
             const m::user::room &user_room)
{
	m::event::append::opts opts;
	opts.event_idx = &event_idx;
	opts.room_depth = &room_depth;
	opts.user_room = &user_room;
	opts.user_id = &user_room.user.user_id;
	return m::event::append
	{
		out, event, opts
	};
}
