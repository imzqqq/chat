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
	"federation event_auth (undocumented)"
};

m::resource
event_auth_resource
{
	"/chat/federation/v1/event_auth/",
	{
		"federation event_auth",
		resource::DIRECTORY,
	}
};

conf::item<size_t>
event_auth_flush_hiwat
{
	{ "name",     "ircd.federation.event_auth.flush.hiwat" },
	{ "default",  16384L                                   },
};

m::resource::response
get__event_auth(client &client,
                const m::resource::request &request)
{
	if(request.parv.size() < 1)
		throw m::NEED_MORE_PARAMS
		{
			"room_id path parameter required"
		};

	m::room::id::buf room_id
	{
		url::decode(room_id, request.parv[0])
	};

	if(m::room::server_acl::enable_read && !m::room::server_acl::check(room_id, request.node_id))
		throw m::ACCESS_DENIED
		{
			"You are not permitted by the room's server access control list."
		};

	if(request.parv.size() < 2)
		throw m::NEED_MORE_PARAMS
		{
			"event_id path parameter required"
		};

	m::event::id::buf event_id
	{
		url::decode(event_id, request.parv[1])
	};

	const m::room room
	{
		room_id, event_id
	};

	bool visible
	{
		m::visible(room, request.node_id)
	};

	// make an exception to the visibility for invitee cases.
	if(!visible)
	{
		static const m::event::fetch::opts fopts
		{
			m::event::keys::include {"room_id", "sender", "type", "state_key", "content"}
		};

		const m::event::fetch event
		{
			event_id, fopts
		};

		visible = m::visible(event, request.node_id);
	}

	if(!visible)
		throw m::ACCESS_DENIED
		{
			"You are not permitted to view the room at this event"
		};

	const m::room::auth::chain chain
	{
		m::index(event_id)
	};

	m::resource::response::chunked response
	{
		client, http::OK
	};

	json::stack out
	{
		response.buf, response.flusher(), size_t(event_auth_flush_hiwat)
	};

	json::stack::object top
	{
		out
	};

	if(request.query.get<bool>("auth_chain", true))
	{
		json::stack::array auth_chain
		{
			top, "auth_chain"
		};

		m::event::fetch event;
		chain.for_each([&auth_chain, &event]
		(const m::event::idx &event_idx)
		{
			if(likely(seek(std::nothrow, event, event_idx)))
				auth_chain.append(event);

			return true;
		});
	}

	if(request.query.get<bool>("auth_chain_ids", false))
	{
		json::stack::array auth_chain_ids
		{
			top, "auth_chain_ids"
		};

		chain.for_each([&auth_chain_ids]
		(const m::event::idx &event_idx)
		{
			m::event_id(std::nothrow, event_idx, [&auth_chain_ids]
			(const m::event::id &event_id)
			{
				auth_chain_ids.append(event_id);
			});

			return true;
		});
	}

	return std::move(response);
}

m::resource::method
method_get
{
	event_auth_resource, "GET", get__event_auth,
	{
		method_get.VERIFY_ORIGIN
	}
};
