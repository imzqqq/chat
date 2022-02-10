// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.


namespace ircd::m::sync
{
	struct response;

	static const_buffer flush(data &, resource::response::chunked &, const const_buffer &);
	static bool empty_response(data &, const uint64_t &next_batch);
	static bool linear_handle(data &);
	static bool polylog_handle(data &);
	static bool longpoll_handle(data &);
	static resource::response handle_get(client &, const resource::request &);

	extern conf::item<size_t> flush_hiwat;
	extern conf::item<size_t> buffer_size;
	extern conf::item<size_t> linear_buffer_size;
	extern conf::item<size_t> linear_delta_max;
	extern conf::item<bool> longpoll_enable;
	extern conf::item<bool> polylog_phased;
	extern conf::item<bool> polylog_only;
	extern conf::item<bool> MSC2855;
	extern conf::item<std::string> pause;

	extern resource::method method_get;
	extern const string_view description;
	extern resource resource;
}

namespace ircd::m::sync::longpoll
{
	static void fini() noexcept;
}

ircd::mapi::header
IRCD_MODULE
{
	"Client 6.2.1 :Sync", nullptr, []
	{
		ircd::m::sync::longpoll::fini();
	}
};

decltype(ircd::m::sync::resource)
ircd::m::sync::resource
{
	"/chat/client/r0/sync",
	{
		description
	}
};

decltype(ircd::m::sync::description)
ircd::m::sync::description
{R"(6.2.1

Synchronise the client's state with the latest state on the server. Clients
use this API when they first log in to get an initial snapshot of the state
on the server, and then continue to call this API to get incremental deltas
to the state, and to receive new messages.
)"};

const auto linear_delta_max_help
{R"(

Maximum number of events to scan sequentially for a /sync. This determines
whether linear-sync or polylog-sync mode is used to satisfy the request. If
the difference between the since token (lower-bound) and the upper-bound of
the sync is within this value, the linear-sync mode is used. If it is more
than this value a polylog-sync mode is used. The latter is used because at
some threshold it becomes too expensive to scan a huge number of events to
grab only those that the client requires; it is cheaper to conduct a series
of random-access queries with polylog-sync instead. Note the exclusive
upper-bound of a sync is determined either by a non-spec query parameter
'next_batch' or the vm::sequence::retired+1.

)"};

const auto linear_buffer_size_help
{R"(

The size of the coalescing buffer used when conducting a linear-sync. During
the sequential scan of events, when an event is marked as required for the
client's sync it is stringified and appended to this buffer. The buffer has
the format of a json::vector of individual events. At the end of the linear
sync, the objects in this buffer are merged into a single spec /sync response.

When this buffer is full the linear sync must finish and respond to the client
with whatever it has. The event::idx of the last event that fit into the buffer
forms the basis for the next_batch so the client can continue with another linear
/sync to complete the range.

)"};

decltype(ircd::m::sync::flush_hiwat)
ircd::m::sync::flush_hiwat
{
	{ "name",     "ircd.client.sync.flush.hiwat" },
	{ "default",  long(64_KiB)                   },
};

decltype(ircd::m::sync::buffer_size)
ircd::m::sync::buffer_size
{
	{ "name",     "ircd.client.sync.buffer_size" },
	{ "default",  long(512_KiB)                  },
	{ "help",     "Response chunk buffer size"   },
};

decltype(ircd::m::sync::linear_buffer_size)
ircd::m::sync::linear_buffer_size
{
	{ "name",     "ircd.client.sync.linear.buffer_size" },
	{ "default",  long(256_KiB)                         },
	{ "help",     linear_buffer_size_help               },
};

decltype(ircd::m::sync::linear_delta_max)
ircd::m::sync::linear_delta_max
{
	{ "name",     "ircd.client.sync.linear.delta.max"  },
	{ "default",  1024                                 },
	{ "help",     linear_delta_max_help                },
};

decltype(ircd::m::sync::polylog_phased)
ircd::m::sync::polylog_phased
{
	{ "name",     "ircd.client.sync.polylog.phased" },
	{ "default",  true                              },
};

decltype(ircd::m::sync::polylog_only)
ircd::m::sync::polylog_only
{
	{ "name",     "ircd.client.sync.polylog.only" },
	{ "default",  false                           },
};

decltype(ircd::m::sync::longpoll_enable)
ircd::m::sync::longpoll_enable
{
	{ "name",     "ircd.client.sync.longpoll.enable" },
	{ "default",  true                               },
};

decltype(ircd::m::sync::MSC2855)
ircd::m::sync::MSC2855
{
	{ "name",     "ircd.client.sync.MSC2855.enable" },
	{ "default",  true                              },
};

decltype(ircd::m::sync::pause)
ircd::m::sync::pause
{
	{ "name",     "ircd.client.sync.pause" },
	{ "default",  string_view{}            },
};

//
// GET sync
//

decltype(ircd::m::sync::method_get)
ircd::m::sync::method_get
{
	resource, "GET", handle_get,
	{
		method_get.REQUIRES_AUTH,
		-1s,
	}
};

ircd::resource::response
ircd::m::sync::handle_get(client &client,
                          const resource::request &request)
{
	// Parse the request options
	const args args
	{
		request
	};

	// The range to `/sync`. We involve events starting at the range.first
	// index in this sync. We will not involve events with an index equal
	// or greater than the range.second. In this case the range.second does not
	// exist yet because it is one past the server's sequence::retired counter.
	const m::events::range range
	{
		std::get<0>(args.since),
		std::min(args.next_batch, m::vm::sequence::retired + 1)
	};

	// The phased initial sync feature uses negative since tokens.
	const bool phased_range
	{
		int64_t(range.first) < 0L
	};

	// Check if the admin disabled phased sync.
	if(!polylog_phased && phased_range)
		throw m::NOT_FOUND
		{
			"Since parameter '%ld' must be >= 0.",
			range.first,
		};

	// When the range indexes are the same, the client is polling for the next
	// event which doesn't exist yet. There is no reason for the since parameter
	// to be greater than that, unless it's a negative integer and phased
	// sync is enabled
	const bool invalid_since
	{
		(!polylog_phased || !phased_range)
		&& (range.first > range.second)
	};

	if(unlikely(invalid_since && !MSC2855))
		throw m::NOT_FOUND
		{
			"Since parameter '%lu' is too far in the future."
			" Cannot be greater than '%lu'.",
			range.first,
			range.second
		};

	// Query and cache the device ID for the access token of this request on
	// the stack here for this sync.
	const device::id::buf device_id
	{
		m::user::tokens::device(request.access_token)
	};

	// Determine if there's a diagnostic hold on this sync based on user or
	// device id. This is for developer and debug use (including client devs).
	const bool paused
	{
		(request.query.get("pause", false) && is_oper(request.user_id))
		|| has(string_view(pause), request.user_id)
		|| has(string_view(pause), device_id)
	};

	// Keep state for statistics of this sync here on the stack.
	stats stats;

	// The ubiquitous /sync data object is constructed on the stack here.
	// This is the main state structure for the sync::item iteration which
	// composes the /sync response.
	data data
	{
		request.user_id,
		range,
		&client,
		nullptr,
		&stats,
		&args,
		device_id,
	};

	// Determine if this is an initial-sync request.
	const bool initial_sync
	{
		range.first == 0UL
	};

	// Conditions for phased sync for this client
	data.phased =
	{
		polylog_phased && args.phased &&
		(
			phased_range ||
			(initial_sync && !std::get<1>(args.since))
		)
	};

	static const http::header response_headers[]
	{
		{ "Cache-Control", "no-cache" },
	};

	// Start the chunked encoded response.
	resource::response::chunked response
	{
		client, http::OK, response_headers, buffer_size
	};

	// Start the JSON stream for this response. As the sync items are iterated
	// the supplied response buffer will be flushed out to the supplied
	// callback; in this case, both are provided by the chunked encoding
	// response. Each flush will create and send a chunk containing in-progress
	// JSON. This will yield the ircd::ctx as this chunk is copied to the
	// kernel's TCP buffer, providing flow control for the sync composition.
	json::stack out
	{
		response.buf,
		std::bind(sync::flush, std::ref(data), std::ref(response), ph::_1),
		size_t(flush_hiwat)
	};
	data.out = &out;

	log::debug
	{
		log, "request %s", loghead(data)
	};

	// Pre-determine if longpoll sync mode should be used. This may
	// indicate false now but after conducting a linear or even polylog
	// sync if we don't find any events for the client then we might
	// longpoll later.
	const bool should_longpoll
	{
		// longpoll can be disabled by a conf item (for developers).
		longpoll_enable

		// polylog-phased sync and longpoll are totally exclusive.
		&& !data.phased

		// initial_sync cannot hang on a longpoll otherwise bad things clients
		&& !initial_sync

		// When the since token is in advance of the vm sequence number
		// there's no events to consider for a sync.
		&& range.first > vm::sequence::retired

		// Spec sez that when ?full_state=1 to return immediately, so
		// that rules out longpoll
		&& !args.full_state
	};

	// Determine if linear sync mode should be used. If this is not used, and
	// longpoll mode is not used, then polylog mode must be used.
	const bool should_linear
	{
		// There is a conf item (for developers) to force polylog mode.
		!polylog_only

		// polylog-phased sync and linear are totally exclusive.
		&& !data.phased

		// If longpoll was already determined there's no need for linear
		&& !should_longpoll

		// The primary condition for a linear sync is the number of events
		// in the range being considered by the sync. That threshold is
		// supplied by a conf item.
		&& range.second - range.first <= size_t(linear_delta_max)

		// When the semaphore query param is set we don't need linear mode.
		&& !args.semaphore

		// When full_state is requested we skip to polylog sync because those
		// handlers are best suited for syncing a full room state.
		&& !args.full_state
	};

	// Determine if polylog sync mode should be used.
	const bool should_polylog
	{
		// Polylog mode is only used when neither of the other two modes
		// are determined.
		!should_longpoll && !should_linear

		// When the semaphore query param is set we don't need polylog mode.
		&& !args.semaphore
	};

	// The return value from the operation will be false if no output was
	// generated by the sync operation, indicating we should finally send an
	// empty response.
	bool complete
	{
		false
		|| paused
		|| invalid_since
	};

	if(paused)
		ctx::sleep_until(data.args->timesout);

	if(!complete && should_polylog)
		complete = polylog_handle(data);

	if(!complete && should_linear)
		complete = linear_handle(data);

	if(!complete)
		complete = longpoll_handle(data);

	if(!complete || invalid_since || paused)
		complete = empty_response(data, uint64_t
		{
			invalid_since?
				0UL:

			polylog_only?
				data.range.first:

			paused?
				data.range.first:

			data.range.second
		});

	assert(complete);
	return std::move(response);
}

bool
ircd::m::sync::empty_response(data &data,
                              const uint64_t &next_batch)
{
	json::stack::object top
	{
		*data.out
	};

	// Empty objects added to output otherwise Riot b0rks.
	json::stack::object
	{
		top, "rooms"
	};

	json::stack::object
	{
		top, "presence"
	};

	char buf[64];
	json::stack::member
	{
		top, "next_batch", json::value
		{
			make_since(buf, next_batch), json::STRING
		}
	};

	const auto &reason
	{
		next_batch?
			"timeout"_sv:
			"MSC2855 clear cache & reload"_sv
	};

	const auto &level
	{
		next_batch?
			log::DEBUG:
			log::WARNING
	};

	log::logf
	{
		log, level,
		"request %s %s @%lu",
		loghead(data),
		reason,
		next_batch,
	};

	return true;
}

ircd::const_buffer
ircd::m::sync::flush(data &data,
                     resource::response::chunked &response,
                     const const_buffer &buffer)
{
	assert(size(buffer) <= size(response.buf));
	const auto wrote
	{
		response.flush(buffer)
	};

	assert(size(wrote) <= size(buffer));
	if(data.stats)
	{
		data.stats->flush_bytes += size(wrote);
		data.stats->flush_count++;
	}

	return wrote;
}

///////////////////////////////////////////////////////////////////////////////
//
// longpoll
//

namespace ircd::m::sync
{
	// fwd decl as longpoll is a frontend to a linear-sync.
	static size_t linear_proffer_event(data &, const mutable_buffer &);
}

namespace ircd::m::sync::longpoll
{
	static bool polled(data &, const args &);
	static int poll(data &);
	static void handle_notify(const m::event &, m::vm::eval &);
	static void fini() noexcept;

	extern m::hookfn<m::vm::eval &> notified;
	extern ctx::dock dock;
}

decltype(ircd::m::sync::longpoll::dock)
ircd::m::sync::longpoll::dock;

decltype(ircd::m::sync::longpoll::notified)
ircd::m::sync::longpoll::notified
{
	handle_notify,
	{
		{ "_site",  "vm.notify" },
	}
};

void
ircd::m::sync::longpoll::fini()
noexcept
{
	if(!dock.empty())
		log::warning
		{
			log, "Interrupting %zu longpolling clients...",
			dock.size(),
		};

	interrupt(dock);
}

void
ircd::m::sync::longpoll::handle_notify(const m::event &event,
                                       m::vm::eval &eval)
try
{
	assert(eval.opts);
	if(!eval.opts->notify_clients)
		return;

	dock.notify_all();
}
catch(const ctx::interrupted &)
{
	throw;
}
catch(const std::exception &e)
{
	log::critical
	{
		log, "request %s longpoll notify :%s",
		loghead(eval),
		e.what(),
	};
}

/// Longpolling blocks the client's request until a relevant event is processed
/// by the m::vm. If no event is processed by a timeout this returns false.
bool
ircd::m::sync::longpoll_handle(data &data)
try
{
	int ret;
	while((ret = longpoll::poll(data)) == -1)
	{
		// When the client explicitly gives a next_batch token we have to
		// adhere to it and return an empty response before going past their
		// desired upper-bound for this /sync.
		assert(data.args);
		if(int64_t(data.args->next_batch) > 0)
			if(data.range.first >= data.range.second || data.range.second >= vm::sequence::retired)
				return false;

		data.range.second = std::min(data.range.second + 1, vm::sequence::retired + 1);
		assert(data.range.second <= vm::sequence::retired + 1);
		assert(data.range.first <= data.range.second);
	}

	return ret;
}
catch(const std::system_error &e)
{
	log::derror
	{
		log, "longpoll %s failed :%s",
		loghead(data),
		e.what()
	};

	throw;
}
catch(const std::exception &e)
{
	log::error
	{
		log, "longpoll %s FAILED :%s",
		loghead(data),
		e.what()
	};

	throw;
}

/// When the vm's sequence number is incremented our dock is notified and the
/// event at that next sequence number is fetched. That event gets proffered
/// around the linear sync handlers for whether it's relevant to the user
/// making the request on this stack.
///
/// If relevant, we respond immediately with that one event and finish the
/// request right there, providing them the next since token of one-past the
/// event_idx that was just synchronized.
///
/// If not relevant, we send nothing and continue checking events that come
/// through until the timeout. This will be an empty response providing the
/// client with the next since token of one past where we left off (vm's
/// current sequence number) to start the next /sync.
///
/// @returns
/// - true if a relevant event was hit and output to the client. If so, this
/// request is finished and nothing else can be sent to the client.
/// - false if a timeout occurred. Nothing was sent to the client so the
/// request must be finished upstack, or an exception may be thrown, etc.
/// - -1 to continue the polling loop when no relevant event was hit. Nothing
/// has been sent to the client yet here either.
///
int
ircd::m::sync::longpoll::poll(data &data)
{
	const auto ready{[&data]
	{
		assert(data.range.second <= m::vm::sequence::retired + 1);
		return data.range.second <= m::vm::sequence::retired;
	}};

	assert(data.args);
	if(!dock.wait_until(data.args->timesout, ready))
		return false;

	// Check if client went away while we were sleeping,
	// if so, just returning true is the easiest way out w/o throwing
	assert(data.client && data.client->sock);
	if(unlikely(!data.client || !data.client->sock))
		return true;

	// slightly more involved check of the socket before
	// we waste resources on the operation; throws.
	const auto &client(*data.client);
	net::check(*client.sock);

	// Keep in mind if the handler returns true that means
	// it made a hit and we can return true to exit longpoll
	// and end the request cleanly.
	if(polled(data, *data.args))
		return true;

	return -1;
}

/// Evaluate the event indexed by data.range.second (the upper-bound). The
/// sync system sees a data.range window of [since, U] where U is a counter
/// that starts at the `vm::sequence::retired` event_idx
bool
ircd::m::sync::longpoll::polled(data &data,
                                const args &args)
{
	// Increment one past-the-end.
	const scope_restore range
	{
		data.range.second, data.range.second + 1
	};

	assert(data.range.second - 1 <= m::vm::sequence::retired);
	const m::event::fetch event
	{
		std::nothrow, data.range.second - 1
	};

	if(!event.valid)
		return false;

	const scope_restore their_event
	{
		data.event, &event
	};

	assert(event.event_idx <= m::vm::sequence::retired);
	const scope_restore their_event_idx
	{
		data.event_idx, event.event_idx
	};

	const unique_buffer<mutable_buffer> scratch
	{
		128_KiB
	};

	const size_t consumed
	{
		linear_proffer_event(data, scratch)
	};

	// In semaphore-mode we're just here to ride the longpoll's blocking
	// behavior. We want the client to get an empty response.
	if(args.semaphore)
		return false;

	if(!consumed && !data.reflow_full_state)
		return false;

	assert(!data.reflow_full_state || data.event_idx);
	const auto next
	{
		data.event_idx && data.reflow_full_state?
			std::min(data.event_idx, vm::sequence::retired + 1):

		data.event_idx?
			std::min(data.event_idx + 1, vm::sequence::retired + 1):

		std::min(data.range.second, vm::sequence::retired + 1)
	};

	const auto &flags
	{
		data.reflow_full_state?
			"P"_sv:
			string_view{}
	};

	const json::vector vector
	{
		string_view
		{
			buffer::data(scratch), consumed
		}
	};

	json::stack::object top
	{
		*data.out
	};

	if(likely(consumed))
		json::merge(top, vector);

	char since_buf[64];
	json::stack::member
	{
		top, "next_batch", json::value
		{
			make_since(since_buf, next, flags), json::STRING
		}
	};

	log::debug
	{
		log, "request %s longpoll hit:%lu consumed:%zu complete @%lu",
		loghead(data),
		event.event_idx,
		consumed,
		next
	};

	return true;
}

///////////////////////////////////////////////////////////////////////////////
//
// linear
//

// Approach for small `since` ranges. The range of events is iterated and
// the event itself is presented to each handler in the schema. This also
// involves a json::stack trace of the schema so that if the handler determines
// the event is appropriate for syncing to the user the output buffer will
// contain a residue of a /sync response with a single event.
//
// After the iteration of events is complete we are left with several buffers
// of properly formatted individual /sync responses which we rewrite into a
// single response to overcome the inefficiency of request ping-pong under
// heavy load.

namespace ircd::m::sync
{
	static bool linear_proffer_event_one(data &);
	static size_t linear_proffer_event(data &, const mutable_buffer &);
	static std::pair<event::idx, bool> linear_proffer(data &, window_buffer &);
}

bool
ircd::m::sync::linear_handle(data &data)
try
{
	assert(data.event_idx <= m::vm::sequence::retired);
	json::stack::checkpoint checkpoint
	{
		*data.out
	};

	json::stack::object top
	{
		*data.out
	};

	const unique_buffer<mutable_buffer> buf
	{
		// must be at least worst-case size of m::event plus some.
		std::max(size_t(linear_buffer_size), size_t(128_KiB))
	};

	window_buffer wb{buf};
	const auto &[last, completed]
	{
		linear_proffer(data, wb)
	};

	const json::vector vector
	{
		wb.completed()
	};

	const auto next
	{
		last && data.reflow_full_state?
			std::min(last, data.range.second):

		last && completed?
			data.range.second:

		last?
			std::min(last + 1, data.range.second):

		0UL
	};

	assert(!data.reflow_full_state || (last && !completed));

	if(last)
	{
		const auto &flags
		{
			data.reflow_full_state?
				"P"_sv:
				string_view{}
		};

		char buf[64];
		json::stack::member
		{
			top, "next_batch", json::value
			{
				make_since(buf, next, flags), json::STRING
			}
		};

		json::merge(top, vector);
	}
	else checkpoint.committing(false);

	log::debug
	{
		log, "request %s linear last:%lu %s@%lu events:%zu",
		loghead(data),
		last,
		completed? "complete "_sv : string_view{},
		next,
		vector.size(),
	};

	return last;
}
catch(const std::exception &e)
{
	log::error
	{
		log, "linear %s FAILED :%s",
		loghead(data),
		e.what()
	};

	throw;
}

/// Iterates the events in the data.range and creates a json::vector in
/// the supplied window_buffer. The return value is the event_idx of the
/// last event which fit in the buffer, or 0 of nothing was of interest
/// to our client in the event iteration.
std::pair<ircd::m::event::idx, bool>
ircd::m::sync::linear_proffer(data &data,
                              window_buffer &wb)
{
	event::idx ret(0);
	const auto closure{[&data, &wb, &ret]
	(const m::event::idx &event_idx, const m::event &event)
	{
		assert(event_idx <= m::vm::sequence::retired);
		const scope_restore their_event
		{
			data.event, &event
		};

		const scope_restore their_event_idx
		{
			data.event_idx, event_idx
		};

		wb([&data, &ret, &event_idx]
		(const mutable_buffer &buf)
		{
			const auto consumed
			{
				linear_proffer_event(data, buf)
			};

			if(consumed)
				ret = event_idx;

			return consumed;
		});

		const bool enough_space_for_more
		{
			// The buffer must have at least this much more space
			// to continue with the iteration. Otherwise if the next
			// worst-case event does not fit, bad things.
			wb.remaining() >= 68_KiB

			// When the handler reports this special-case we have
			// to stop at this iteration.
			&& !data.reflow_full_state
		};

		return enough_space_for_more;
	}};

	const auto completed
	{
		m::events::for_each(data.range, closure)
	};

	return
	{
		ret, completed
	};
}

/// Sets up a json::stack for the iteration of handlers for
/// one event.
size_t
ircd::m::sync::linear_proffer_event(data &data,
                                    const mutable_buffer &buf)
{
	json::stack out{buf};
	const scope_restore their_out
	{
		data.out, &out
	};

	json::stack::object top
	{
		*data.out
	};

	const bool success
	{
		linear_proffer_event_one(data)
	};

	top.~object();
	return success?
		size(out.completed()):
		0UL;
}

/// Generates a candidate /sync response for a single event by
/// iterating all of the handlers.
bool
ircd::m::sync::linear_proffer_event_one(data &data)
{
	bool ret{false};
	m::sync::for_each(string_view{}, [&data, &ret]
	(item &item)
	{
		json::stack::checkpoint checkpoint
		{
			*data.out
		};

		if(item.linear(data))
			ret = true;
		else
			checkpoint.rollback();

		return true;
	});

	return ret;
}

///////////////////////////////////////////////////////////////////////////////
//
// polylog
//

// Random access approach for large `since` ranges. The /sync schema itself is
// recursed. For every component in the schema, the handler seeks the events
// appropriate for the user and appends it to the output. Concretely, this
// involves a full iteration of the rooms a user is a member of, and a full
// iteration of the presence status for all users visible to a user, etc.
//
// This entire process occurs in a single pass. The schema is traced with
// json::stack and its buffer is flushed to the client periodically with
// chunked encoding.

bool
ircd::m::sync::polylog_handle(data &data)
try
{
	json::stack::checkpoint checkpoint
	{
		*data.out
	};

	json::stack::object top
	{
		*data.out
	};

	// Prefetch loop
	if(data.range.first == 0)
	{
		const scope_restore prefetching
		{
			data.prefetch, true
		};

		m::sync::for_each(string_view{}, [&data]
		(item &item)
		{
			json::stack::checkpoint checkpoint
			{
				*data.out
			};

			json::stack::object object
			{
				*data.out, item.member_name()
			};

			item.polylog(data);
			checkpoint.committing(false);
			return true;
		});
	}

	// Output loop
	bool ret{false};
	m::sync::for_each(string_view{}, [&data, &ret]
	(item &item)
	{
		json::stack::checkpoint checkpoint
		{
			*data.out
		};

		json::stack::object object
		{
			*data.out, item.member_name()
		};

		if(item.polylog(data))
		{
			ret = true;
			data.out->invalidate_checkpoints();
		}
		else checkpoint.committing(false);

		return true;
	});

	if(ret)
	{
		const int64_t next_batch
		{
			data.phased?
				int64_t(data.range.first) - 1L:
				int64_t(data.range.second)
		};

		char buf[64];
		assert(data.phased || next_batch >= 0L);
		const string_view &next_batch_token
		{
			// The polylog phased since token. We pack two numbers separted by a '_'
			// character which cannot be urlencoded atm. The first is the usual
			// since token integer, which is negative for phased initial sync. The
			// second part is the next_batch upper-bound integer which is a snapshot
			// of the server's sequence number when the phased sync started.
			data.phased?
				make_since(buf, m::events::range{uint64_t(next_batch), data.range.second}):

			// The normal integer since token.
				make_since(buf, next_batch)
		};

		json::stack::member
		{
			*data.out, "next_batch", json::value
			{
				next_batch_token, json::STRING
			}
		};
	}

	if(!ret)
		checkpoint.committing(false);

	const auto &log_level
	{
		!data.phased && stats::info?
			log::level::INFO:
			log::level::DEBUG
	};

	log::logf
	{
		log, log_level,
		"request %s polylog commit:%b complete @%ld",
		loghead(data),
		ret,
		data.phased?
			data.range.first:
			data.range.second
	};

	return ret;
}
catch(const std::exception &e)
{
	log::error
	{
		log, "polylog %s FAILED :%s",
		loghead(data),
		e.what()
	};

	throw;
}
