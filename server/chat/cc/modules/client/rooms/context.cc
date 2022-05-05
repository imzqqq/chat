// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

#include "rooms.h"

using namespace ircd;

/// 11.20.1.1 - The maximum number of events to return. Default: 10.
const size_t
default_limit
{
	10
};

conf::item<size_t>
limit_max
{
	{ "name",     "ircd.client.rooms.context.limit.max" },
	{ "default",  128L                                  },
};

conf::item<size_t>
flush_hiwat
{
	{ "name",     "ircd.client.rooms.context.flush.hiwat" },
	{ "default",  16384L                                  },
};

log::log
context_log
{
	"m.context"
};

static bool
_append(json::stack::array &,
        const m::event &,
        const m::event::idx &,
        const m::user::room &,
        const int64_t &,
        const bool &query_txnid = true);

m::resource::response
get__context(client &client,
             const m::resource::request &request,
             const m::room::id &room_id)
{
	if(request.parv.size() < 3)
		throw m::NEED_MORE_PARAMS
		{
			"event_id path parameter required"
		};

	m::event::id::buf event_id
	{
		url::decode(event_id, request.parv[2])
	};

	const auto &limit
	{
		std::min(request.query.get<size_t>("limit", default_limit), size_t(limit_max))
	};

	const m::room room
	{
		room_id, event_id
	};

	if(!visible(room, request.user_id))
		throw m::ACCESS_DENIED
		{
			"You are not permitted to view the room at this event"
		};

	// Non-spec param to allow preventing any state from being returned.
	const bool include_state
	{
		request.query.get("state", true)
	};

	// The standard ?filter= is parsed here. m::filter::get() handles
	// whether this is a filter_id and conducts a fetch into this buffer;
	// or inline JSON, and performs URL decoding into this buffer.
	const std::string filter_json
	{
		m::filter::get(request.query["filter"], request.user_id)
	};

	const m::room_event_filter filter
	{
		filter_json
	};

	const m::event::fetch event
	{
		event_id
	};

	const m::user::room &user_room
	{
		request.user_id
	};

	const auto room_depth
	{
		m::depth(std::nothrow, room_id)
	};

	m::resource::response::chunked response
	{
		client, http::OK
	};

	json::stack out
	{
		response.buf, response.flusher(), size_t(flush_hiwat)
	};

	json::stack::object ret
	{
		out
	};

	// Output the main event first.
	{
		json::stack::object _event
		{
			ret, "event"
		};

		// We use m::event::append() to modify/add/remove data for this client.
		m::event::append::opts opts;
		opts.event_idx = &event.event_idx;
		opts.user_id = &user_room.user.user_id;
		opts.user_room = &user_room;
		opts.room_depth = &room_depth;
		m::event::append(_event, event, opts);
	}

	// Counters for debug messages
	struct counts
	{
		size_t before {0};
		size_t after {0};
		size_t state {0};
	}
	counts;

	m::event::id::buf start;
	{
		json::stack::array array
		{
			ret, "events_before"
		};

		m::room::events before
		{
			room, event_id
		};

		if(before)
			--before;

		for(size_t i(0); i < limit && before; --before, ++i)
		{
			const m::event &event{*before};
			start = event.event_id;
			if(!visible(event, request.user_id))
				continue;

			counts.before += _append(array, event, before.event_idx(), user_room, room_depth);
		}

		if(before && limit > 0)
			--before;

		if(before)
			start = m::event_id(before.event_idx());
		else
			start = {};
	}

	if(start)
		json::stack::member
		{
			ret, "start", json::value{start}
		};

	m::event::id::buf end;
	{
		json::stack::array array
		{
			ret, "events_after"
		};

		m::room::events after
		{
			room, event_id
		};

		if(after)
			++after;

		for(size_t i(0); i < limit && after; ++after, ++i)
		{
			const m::event &event{*after};
			end = event.event_id;
			if(!visible(event, request.user_id))
				continue;

			counts.after += _append(array, event, after.event_idx(), user_room, room_depth);
		}

		if(after && limit > 0)
			++after;

		if(after)
			end = m::event_id(after.event_idx());
		else
			end = {};
	}

	if(end)
		json::stack::member
		{
			ret, "end", json::value{end}
		};

	if(include_state)
	{
		json::stack::array array
		{
			ret, "state"
		};

		const m::room::state state
		{
			room
		};

		// Setup the event::fetch instance outside of the closure to avoid
		// underlying reconstruction costs for now.
		m::event::fetch event;

		// Iterate the state.
		state.for_each([&]
		(const string_view &type, const string_view &state_key, const m::event::idx &event_idx)
		{
			// Conditions to decide if we should skip this state event based
			// on the lazy-loading spec.
			const bool lazy_loaded
			{
				// The user supplied a filter enabling lazy-loading.
				json::get<"lazy_load_members"_>(filter) &&

				// The type of this state event is a m.room.member type
				type == "m.room.member"
			};

			if(lazy_loaded)
				return true;

			if(!seek(std::nothrow, event, event_idx))
				return true;

			if(!visible(event, request.user_id))
				return true;

			counts.state += _append(array, event, event_idx, user_room, room_depth, false);
			return true;
		});
	}

	log::debug
	{
		context_log, "%s %s in %s before:%zu start:%s after:%zu end:%s state:%zu",
		client.loghead(),
		string_view{event_id},
		string_view{room_id},
		counts.before,
		string_view{start},
		counts.after,
		string_view{end},
		counts.state,
	};

	return std::move(response);
}

bool
_append(json::stack::array &chunk,
        const m::event &event,
        const m::event::idx &event_idx,
        const m::user::room &user_room,
        const int64_t &room_depth,
        const bool &query_txnid)
{
	m::event::append::opts opts;
	opts.event_idx = &event_idx;
	opts.user_id = &user_room.user.user_id;
	opts.user_room = &user_room;
	opts.room_depth = &room_depth;
	return m::event::append(chunk, event, opts);
}
