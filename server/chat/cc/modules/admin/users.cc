// The Construct
//
// Copyright (C) The Construct Developers, Authors & Contributors
// Copyright (C) 2016-2020 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

namespace ircd::m::admin
{
	static resource::response handle_get_admin(client &, const resource::request &, const user::id &);
	static resource::response handle_get(client &, const resource::request &);

	extern resource::method get_method;
	extern resource users_resource;
};

ircd::mapi::header
IRCD_MODULE
{
	"Admin (undocumented) :Users"
};

decltype(ircd::m::admin::users_resource)
ircd::m::admin::users_resource
{
	"/_chat/admin/v1/users/",
	{
		"(undocumented) Admin Users",
		resource::DIRECTORY
	}
};

decltype(ircd::m::admin::get_method)
ircd::m::admin::get_method
{
	users_resource, "GET", handle_get,
	{
		get_method.REQUIRES_AUTH
	}
};

ircd::m::resource::response
ircd::m::admin::handle_get(client &client,
                           const resource::request &request)
{
	if(request.parv.size() < 1)
		throw m::NEED_MORE_PARAMS
		{
			"user_id path parameter required"
		};

	user::id::buf user_id
	{
		url::decode(user_id, request.parv[0])
	};

	if(request.parv.size() < 2)
		throw m::NEED_MORE_PARAMS
		{
			"Command path parameter required"
		};

	const auto &cmd
	{
		request.parv[1]
	};

	if(cmd == "admin")
		return handle_get_admin(client, request, user_id);

	throw m::NOT_FOUND
	{
		"/admin/users command not found"
	};
}

/// Return if a user is an admin
ircd::m::resource::response
ircd::m::admin::handle_get_admin(client &client,
                                 const resource::request &request,
                                 const user::id &user_id)
{
	const bool admin
	{
		m::is_oper(user_id)
	};

	return m::resource::response
	{
		client, json::members
		{
			{ "admin", admin }
		}
	};
}
