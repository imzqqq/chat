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

static m::resource::response
get__list_room(client &client,
               const m::resource::request &request);

static m::resource::response
put__list_room(client &client,
               const m::resource::request &request);

mapi::header
IRCD_MODULE
{
	"Client 10.5 :Listing rooms"
};

m::resource
list_room_resource
{
	"/chat/client/r0/directory/list/room/",
	{
		"(10.5) Listing rooms",
		list_room_resource.DIRECTORY
	}
};

m::resource::method
list_room_put
{
	list_room_resource, "PUT", put__list_room,
	{
		list_room_put.REQUIRES_AUTH
	}
};

m::resource::response
put__list_room(client &client,
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

	const m::room room
	{
		room_id
	};

	if(!exists(room))
		throw m::NOT_FOUND
		{
			"Room %s is not known to this server",
			string_view{room_id}
		};

	const m::room::power power
	{
		room
	};

	const bool permitted
	{
		// is_oper(request.user_id) ||
		power(request.user_id, {}, "m.room.history_visibility", "") &&
		power(request.user_id, {}, "m.room.join_rules", "")
	};

	if(!permitted)
		throw m::ACCESS_DENIED
		{
			"You do not have permission to list the room on this server"
		};

	const json::string &visibility
	{
		request.at("visibility")
	};

	switch(hash(visibility))
	{
		case "public"_:
			// We set an empty summary for this room because
			// we already have its state on this server;
			m::rooms::summary::set(room.room_id);
			break;

		case "private"_:
			m::rooms::summary::del(room.room_id);
			break;

		default: throw m::UNSUPPORTED
		{
			"visibility type '%s' is not supported here",
			string_view{visibility}
		};
	}

	return m::resource::response
	{
		client, http::OK
	};
}

m::resource::method
list_room_get
{
	list_room_resource, "GET", get__list_room
};

m::resource::response
get__list_room(client &client,
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

	const m::room room
	{
		room_id
	};

	if(!exists(room))
		throw m::NOT_FOUND
		{
			"Room %s is not known to this server",
			string_view{room_id}
		};

	const string_view &visibility
	{
		m::rooms::summary::has(room_id)? "public" : "private"
	};

	return m::resource::response
	{
		client, json::members
		{
			{ "visibility", visibility }
		}
	};
}
