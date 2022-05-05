// The Construct
//
// Copyright (C) The Construct Developers, Authors & Contributors
// Copyright (C) 2016-2020 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

namespace ircd::m
{
	static resource::response get_room_keys_keys(client &, const resource::request &);
	extern resource::method room_keys_keys_get;

	static event::id::buf put_room_keys_keys_key(client &, const resource::request &, const room::id &, const string_view &, const event::idx &, const json::object &);
	static resource::response put_room_keys_keys(client &, const resource::request &);
	extern resource::method room_keys_keys_put;

	static resource::response delete_room_keys_keys(client &, const resource::request &);
	extern resource::method room_keys_keys_delete;

	extern resource room_keys_keys;
}

ircd::mapi::header
IRCD_MODULE
{
	"Client (undocumented) :e2e Room Keys Keys"
};

decltype(ircd::m::room_keys_keys)
ircd::m::room_keys_keys
{
	"/chat/client/unstable/room_keys/keys",
	{
		"(undocumented) Room Keys Keys",
		resource::DIRECTORY,
	}
};

//
// DELETE
//

decltype(ircd::m::room_keys_keys_delete)
ircd::m::room_keys_keys_delete
{
	room_keys_keys, "DELETE", delete_room_keys_keys,
	{
		room_keys_keys_delete.REQUIRES_AUTH
	}
};

ircd::m::resource::response
ircd::m::delete_room_keys_keys(client &client,
                               const resource::request &request)
{

	return resource::response
	{
		client, http::NOT_IMPLEMENTED
	};
}

//
// PUT
//

decltype(ircd::m::room_keys_keys_put)
ircd::m::room_keys_keys_put
{
	room_keys_keys, "PUT", put_room_keys_keys,
	{
		// Flags
		room_keys_keys_put.REQUIRES_AUTH,

		// timeout //TODO: XXX designated
		30s,

		// Payload maximum
		1_MiB,
	}
};

ircd::m::resource::response
ircd::m::put_room_keys_keys(client &client,
                            const resource::request &request)
{
	char room_id_buf[room::id::buf::SIZE];
	const string_view &room_id
	{
		request.parv.size() > 0?
			url::decode(room_id_buf, request.parv[0]):
			string_view{}
	};

	char session_id_buf[256];
	const string_view &session_id
	{
		request.parv.size() > 1?
			url::decode(session_id_buf, request.parv[1]):
			string_view{}
	};

	const event::idx version
	{
		request.query.at<event::idx>("version")
	};

	if(!room_id && !session_id)
	{
		const json::object &rooms
		{
			request["rooms"]
		};

		for(const auto &[room_id, sessions] : rooms)
			for(const auto &[session_id, session] : json::object(sessions))
				put_room_keys_keys_key(client, request, room_id, session_id, version, session);
	}
	else if(!session_id)
	{
		const json::object &sessions
		{
			request["sessions"]
		};

		for(const auto &[session_id, session] : sessions)
			put_room_keys_keys_key(client, request, room_id, session_id, version, session);
	}
	else put_room_keys_keys_key(client, request, room_id, session_id, version, request);

	return resource::response
	{
		client, http::OK
	};
}

ircd::m::event::id::buf
ircd::m::put_room_keys_keys_key(client &client,
                                const resource::request &request,
                                const room::id &room_id,
                                const string_view &session_id,
                                const event::idx &version,
                                const json::object &content)
{
	const m::user::room user_room
	{
		request.user_id
	};

	const m::room::type events
	{
		user_room, "ircd.room_keys.version"
	};

	events.for_each([&version]
	(const auto &, const auto &, const event::idx &_event_idx)
	{
		if(m::redacted(_event_idx))
			return true;

		if(_event_idx != version)
			throw http::error
			{
				"%lu is not the most recent key version",
				http::FORBIDDEN,
				version
			};

		return false; // false to break after this first hit
	});

	char state_key_buf[event::STATE_KEY_MAX_SIZE];
	const string_view state_key{fmt::sprintf
	{
		state_key_buf, "%s:%s:%u",
		string_view{room_id},
		session_id,
		version,
	}};

	const auto event_id
	{
		send(user_room, request.user_id, "ircd.room_keys.key", state_key, content)
	};

	return event_id;
}

//
// GET
//

decltype(ircd::m::room_keys_keys_get)
ircd::m::room_keys_keys_get
{
	room_keys_keys, "GET", get_room_keys_keys,
	{
		room_keys_keys_get.REQUIRES_AUTH
	}
};

ircd::m::resource::response
ircd::m::get_room_keys_keys(client &client,
                            const resource::request &request)
{
	char room_id_buf[room::id::buf::SIZE];
	const string_view &room_id
	{
		request.parv.size() > 0?
			url::decode(room_id_buf, request.parv[0]):
			string_view{}
	};

	char session_id_buf[256];
	const string_view &session_id
	{
		request.parv.size() > 1?
			url::decode(session_id_buf, request.parv[1]):
			string_view{}
	};

	const event::idx version
	{
		request.query.at<event::idx>("version")
	};

	const m::user::room user_room
	{
		request.user_id
	};

	const m::room::state state
	{
		user_room
	};

	if(room_id && session_id)
	{
		char state_key_buf[event::STATE_KEY_MAX_SIZE];
		const string_view state_key{fmt::sprintf
		{
			state_key_buf, "%s:%s:%u",
			string_view{room_id},
			session_id,
			version,
		}};

		const auto event_idx
		{
			state.get("ircd.room_keys.key", state_key)
		};

		m::get(event_idx, "content", [&client]
		(const json::object &content)
		{
			resource::response
			{
				client, content
			};
		});

		return {}; // responded from closure
	}

	resource::response::chunked response
	{
		client, http::OK
	};

	json::stack out
	{
		response.buf, response.flusher()
	};

	json::stack::object top
	{
		out
	};

	if(room_id)
	{
		json::stack::object sessions
		{
			top, "sessions"
		};

		state.for_each("ircd.room_keys.key", [&room_id, &version, &sessions]
		(const string_view &type, const string_view &state_key, const event::idx &event_idx)
		{
			string_view part[4]; const auto parts
			{
				tokens(state_key, ":", part)
			};

			const auto &_version{part[3]};
			const auto &_session_id{part[2]};
			const string_view &_room_id
			{
				begin(part[0]), end(part[1])
			};

			assert(m::valid(id::ROOM, _room_id));
			if(_room_id != room_id)
				return true;

			if(_version != lex_cast<event::idx>(version))
				return true;

			m::get(std::nothrow, event_idx, "content", [&sessions, &_session_id]
			(const json::object &session)
			{
				json::stack::member
				{
					sessions, _session_id, session
				};
			});

			return true;
		});

		return {};
	}

	assert(0);
	return {};
}
