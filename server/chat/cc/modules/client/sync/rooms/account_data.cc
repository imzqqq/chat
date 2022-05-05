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
	static bool room_account_data_polylog_tags(data &);
	static bool room_account_data_polylog_events_event(data &, const m::event &);
	static bool room_account_data_polylog_events(data &);
	static bool room_account_data_polylog(data &);

	static bool room_account_data_linear_tags(data &, const m::event &);
	static bool room_account_data_linear_events(data &, const m::event &);
	static bool room_account_data_linear(data &);

	extern item room_account_data;
}

ircd::mapi::header
IRCD_MODULE
{
	"Client Sync :Room Account Data"
};

decltype(ircd::m::sync::room_account_data)
ircd::m::sync::room_account_data
{
	"rooms.account_data",
	room_account_data_polylog,
	room_account_data_linear,
	{
		{ "phased", true }
	}
};

bool
ircd::m::sync::room_account_data_linear(data &data)
{
	if(!data.event || !data.event_idx)
		return false;

	const m::event &event{*data.event};
	if(json::get<"room_id"_>(event) != data.user_room.room_id)
		return false;

	json::stack::checkpoint checkpoint
	{
		*data.out
	};

	if(room_account_data_linear_events(data, event))
		return true;

	checkpoint.rollback();
	if(room_account_data_linear_tags(data, event))
		return true;

	checkpoint.rollback();
	return false;
}

bool
ircd::m::sync::room_account_data_linear_events(data &data,
                                               const m::event &event)
{
	if(!json::get<"state_key"_>(event))
		return false;

	const auto type
	{
		split(json::get<"type"_>(event), '!')
	};

	if(type.first != "ircd.account_data")
		return false;

	if(!type.second)
		return false;

	const m::room room
	{
		lstrip(json::get<"type"_>(event), type.first)
	};

	char membuf[room::MEMBERSHIP_MAX_SIZE];
	const auto membership
	{
		m::membership(membuf, room, data.user)
	};

	if(!membership)
		return false;

	json::stack::object rooms
	{
		*data.out, "rooms"
	};

	json::stack::object membership_
	{
		*data.out, membership
	};

	json::stack::object room_
	{
		*data.out, room.room_id
	};

	json::stack::object account_data
	{
		*data.out, "account_data"
	};

	json::stack::array array
	{
		*data.out, "events"
	};

	const scope_restore room__{data.room, &room};
	const scope_restore range_upper{data.range.second, data.event_idx + 1};
	return room_account_data_polylog_events_event(data, event);
}

bool
ircd::m::sync::room_account_data_linear_tags(data &data,
                                             const m::event &event)
{
	if(!json::get<"state_key"_>(event))
		return false;

	const auto type
	{
		split(json::get<"type"_>(event), '!')
	};

	if(type.first != "ircd.room_tag")
		return false;

	if(!type.second)
		return false;

	const m::room room
	{
		lstrip(json::get<"type"_>(event), type.first)
	};

	char membuf[room::MEMBERSHIP_MAX_SIZE];
	const auto membership
	{
		m::membership(membuf, room, data.user)
	};

	if(!membership)
		return false;

	json::stack::object rooms
	{
		*data.out, "rooms"
	};

	json::stack::object membership_
	{
		*data.out, membership
	};

	json::stack::object room_
	{
		*data.out, room.room_id
	};

	json::stack::object account_data
	{
		*data.out, "account_data"
	};

	json::stack::array array
	{
		*data.out, "events"
	};

	// Due to room tags being "all one event" we have to iterate all of the
	// tags for this room for this user polylog style. This is because the
	// merge algorithm for linear /sync isn't sophisticated enough to see
	// past the events[] array and know to combine all of room tags into
	// the required format. The event_idx is hacked to 0 here to trick the
	// polylog apropos() into composing all tags unconditionally.
	const scope_restore range_lower{data.range.first, 0UL};
	const scope_restore room__{data.room, &room};
	return room_account_data_polylog_tags(data);
}

bool
ircd::m::sync::room_account_data_polylog(data &data)
{
	json::stack::array array
	{
		*data.out, "events"
	};

	bool ret{false};
	ret |= room_account_data_polylog_events(data);
	ret |= room_account_data_polylog_tags(data);
	return ret;
}

bool
ircd::m::sync::room_account_data_polylog_events(data &data)
{
	assert(data.room);
	char typebuf[m::user::room_account_data::typebuf_size];
	const auto type
	{
		m::user::room_account_data::_type(typebuf, data.room->room_id)
	};

	bool ret{false};
	data.user_state.for_each(type, [&data, &ret]
	(const string_view &type, const string_view &state_key, const m::event::idx &event_idx)
	{
		// We have to ignore the since range here or Riot will forget or overwrite data.
		//if(!apropos(data, event_idx))
		//	return true;

		static const event::fetch::opts fopts
		{
			event::keys::include {"content"}
		};

		m::event::fetch event
		{
			std::nothrow, event_idx, fopts
		};

		if(!event.valid)
			return true;

		json::get<"state_key"_>(event) = state_key;
		ret |= room_account_data_polylog_events_event(data, event);
		return true;
	});

	return ret;
}

bool
ircd::m::sync::room_account_data_polylog_events_event(data &data,
                                                      const m::event &event)
{
	json::stack::object object
	{
		*data.out
	};

	json::stack::member
	{
		object, "type", at<"state_key"_>(event)
	};

	json::stack::member
	{
		object, "content", at<"content"_>(event)
	};

	return true;
}

bool
ircd::m::sync::room_account_data_polylog_tags(data &data)
{
	json::stack::object object
	{
		*data.out
	};

	json::stack::member
	{
		object, "type", "m.tag"
	};

	json::stack::object content
	{
		object, "content"
	};

	json::stack::object tags
	{
		content, "tags"
	};

	assert(data.room);
	char typebuf[m::user::room_tags::typebuf_size];
	const auto type
	{
		m::user::room_tags::_type(typebuf, data.room->room_id)
	};

	bool ret{false};
	data.user_state.for_each(type, [&data, &tags, &ret]
	(const string_view &type, const string_view &state_key, const m::event::idx &event_idx)
	{
		// We have to ignore the since range here or Riot will forget or overwrite tags.
		//if(!apropos(data, event_idx))
		//	return true;

		static const event::fetch::opts fopts
		{
			event::keys::include {"content"}
		};

		const m::event::fetch event
		{
			std::nothrow, event_idx, fopts
		};

		if(!event.valid)
			return true;

		json::stack::member
		{
			tags, state_key, json::get<"content"_>(event)
		};

		ret = true;
		return true;
	});

	return ret;
}
