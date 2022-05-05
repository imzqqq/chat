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
	static bool should_ignore(const data &);

	static bool _rooms_polylog_room(data &, const m::room &);
	static bool _rooms_polylog(data &, const string_view &membership, int64_t &phase);
	static bool rooms_polylog(data &);

	static bool _rooms_linear(data &, const string_view &membership);
	static bool rooms_linear(data &);

	extern item rooms;
}

ircd::mapi::header
IRCD_MODULE
{
	"Client Sync :Rooms"
};

decltype(ircd::m::sync::rooms)
ircd::m::sync::rooms
{
	"rooms", rooms_polylog, rooms_linear,
	{
		{ "phased",   true },
		{ "prefetch", true },
	}
};

bool
ircd::m::sync::rooms_linear(data &data)
{
	assert(data.event);
	const m::room room
	{
		json::get<"room_id"_>(*data.event)?
			m::room::id{json::get<"room_id"_>(*data.event)}:
			m::room::id{}
	};

	const scope_restore their_room
	{
		data.room, &room
	};

	char membuf[room::MEMBERSHIP_MAX_SIZE];
	const string_view &membership
	{
		json::get<"room_id"_>(*data.event)?
			m::membership(membuf, room, data.user):

		string_view{}
	};

	const scope_restore their_membership
	{
		data.membership, membership
	};

	const bool is_own_membership
	{
		json::get<"room_id"_>(*data.event)
		&& json::get<"type"_>(*data.event) == "m.room.member"
		&& json::get<"state_key"_>(*data.event) == data.user.user_id
	};

	const bool is_own_join
	{
		is_own_membership
		&& membership == "join"
	};

	//assert(!is_own_join || m::membership(*data.event) == "join");
	assert(!is_own_join || !!m::membership(membuf, room, data.user));
	assert(is_own_join || !json::get<"room_id"_>(*data.event) || m::exists(room));

	if(should_ignore(data))
		return false;

	const auto room_head
	{
		data.event_idx && room != data.user_room?
			m::head_idx(std::nothrow, room):
			0UL
	};

	//assert(room_head <= m::vm::sequence::retired);
	assert(data.event_idx <= m::vm::sequence::retired);
	const scope_restore their_head
	{
		data.room_head, room_head
	};

	const auto room_depth
	{
		data.event_idx && room != data.user_room?
			m::depth(std::nothrow, room):
			-1L
	};

	const scope_restore their_depth
	{
		data.room_depth, room_depth
	};

	bool ret{false};
	m::sync::for_each("rooms", [&data, &ret]
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

bool
ircd::m::sync::rooms_polylog(data &data)
{
	bool ret{false};
	int64_t phase(0);

	ret |= _rooms_polylog(data, "join", phase);
	if(data.phased && ret)
		return ret;

	ret |= _rooms_polylog(data, "invite", phase);
	if(data.phased && ret)
		return ret;

	ret |= _rooms_polylog(data, "leave", phase);
	if(data.phased && ret)
		return ret;

	ret |= _rooms_polylog(data, "ban", phase);
	if(data.phased && ret)
		return ret;

	return ret;
}

bool
ircd::m::sync::_rooms_polylog(data &data,
                              const string_view &membership,
                              int64_t &phase)
{
	const scope_restore theirs
	{
		data.membership, membership
	};

	json::stack::object object
	{
		*data.out, membership
	};

	bool ret{false};
	const user::rooms::closure_bool closure{[&data, &ret, &phase]
	(const m::room &room, const string_view &membership_)
	{
		if(data.phased)
		{
			if(phase < int64_t(data.range.first) && ret)
				return false;

			if(int64_t(data.range.first) < 0L)
				--phase;

			if(phase > int64_t(data.range.first))
				return true;
		}

		#if defined(RB_DEBUG)
		sync::stats stats
		{
			data.stats && rooms.stats_debug?
				*data.stats:
				sync::stats{}
		};

		if(data.stats)
			stats.timer = timer{};
		#endif

		ret |= _rooms_polylog_room(data, room);

		#if defined(RB_DEBUG)
		thread_local char tmbuf[32];
		if(data.stats && rooms.stats_debug)
			log::debug
			{
				log, "polylog %s %s %s in %s",
				loghead(data),
				membership_,
				string_view{room.room_id},
				ircd::pretty(tmbuf, stats.timer.at<milliseconds>(), true)
			};
		#endif

		return true;
	}};

	const bool done
	{
		data.user_rooms.for_each(membership, closure)
	};

	return ret;
}

bool
ircd::m::sync::_rooms_polylog_room(data &data,
                                   const m::room &room)
{
	const scope_restore theirs
	{
		data.room, &room
	};

	if(should_ignore(data))
		return false;

	json::stack::checkpoint checkpoint
	{
		*data.out
	};

	json::stack::object object
	{
		*data.out, room.room_id
	};

	const auto &[top_event_id, top_depth, top_event_idx]
	{
		m::top(std::nothrow, room)
	};

	const scope_restore their_head
	{
		data.room_head, top_event_idx
	};

	const scope_restore their_depth
	{
		data.room_depth, top_depth
	};

	bool ret{false};
	m::sync::for_each("rooms", [&data, &ret]
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

	if(!ret)
		checkpoint.committing(false);

	return ret;
}

bool
ircd::m::sync::should_ignore(const data &data)
{
	if(data.prefetch)
		return false;

	if(data.membership != "invite")
		return false;

	if(!m::user::ignores::enforce("invites"))
		return false;

	assert(data.room);
	const m::room::state state
	{
		*data.room
	};

	const m::event::idx &event_idx
	{
		state.get(std::nothrow, "m.room.member", data.user.user_id)
	};

	const m::user::ignores ignores
	{
		data.user.user_id
	};

	bool ret{false};
	m::get(std::nothrow, event_idx, "sender", [&ignores, &ret]
	(const m::user::id &sender)
	{
		ret = ignores.has(sender);
	});

	return ret;
}
