// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

#include "user.h"

using namespace ircd;

mapi::header
IRCD_MODULE
{
	"Client X.X :User endpoints"
};

m::resource
user_resource
{
	"/chat/client/r0/user/",
	{
		"User resource",
		resource::DIRECTORY,
	}
};

m::resource::response
get_user(client &client,
         const m::resource::request &request)
{
	if(request.parv.size() < 2)
		throw m::NEED_MORE_PARAMS
		{
			"user_id required"
		};

	m::user::id::buf user_id
	{
		url::decode(user_id, request.parv[0])
	};

	if(request.user_id != user_id)
		throw m::UNSUPPORTED
		{
			"Getting user data as someone else is not yet supported"
		};

	const string_view &cmd
	{
		request.parv[1]
	};

	if(cmd == "filter")
		return get__filter(client, request, user_id);

	if(cmd == "account_data")
		return get__account_data(client, request, user_id);

	if(cmd == "rooms")
		return get__rooms(client, request, user_id);

	throw m::NOT_FOUND
	{
		"/user command not found"
	};
}

m::resource::method
get_method
{
	user_resource, "GET", get_user,
	{
		get_method.REQUIRES_AUTH
	}
};

m::resource::response
post_user(client &client,
          m::resource::request &request)
{
	if(request.parv.size() < 2)
		throw m::NEED_MORE_PARAMS
		{
			"user_id required"
		};

	m::user::id::buf user_id
	{
		url::decode(user_id, request.parv[0])
	};

	if(request.user_id != user_id)
		throw m::UNSUPPORTED
		{
			"Posting user data as someone else is not yet supported"
		};

	const string_view &cmd
	{
		request.parv[1]
	};

	if(cmd == "filter")
		return post__filter(client, request, user_id);

	if(cmd == "openid")
		return post__openid(client, request, user_id);

	throw m::NOT_FOUND
	{
		"/user command not found"
	};
}

m::resource::method
post_method
{
	user_resource, "POST", post_user,
	{
		post_method.REQUIRES_AUTH
	}
};

m::resource::response
put_user(client &client,
         const m::resource::request &request)
{
	if(request.parv.size() < 1)
		throw m::NEED_MORE_PARAMS
		{
			"user_id required"
		};

	m::user::id::buf user_id
	{
		url::decode(user_id, request.parv[0])
	};

	if(request.user_id != user_id)
		throw m::UNSUPPORTED
		{
			"Putting user data as someone else is not yet supported"
		};

	if(request.parv.size() < 2)
		throw m::NEED_MORE_PARAMS
		{
			"user command required"
		};

	const string_view &cmd
	{
		request.parv[1]
	};

	if(cmd == "account_data")
		return put__account_data(client, request, user_id);

	if(cmd == "rooms")
		return put__rooms(client, request, user_id);

	throw m::NOT_FOUND
	{
		"/user command not found"
	};
}

m::resource::method
put_method
{
	user_resource, "PUT", put_user,
	{
		put_method.REQUIRES_AUTH
	}
};

m::resource::response
delete_user(client &client,
            const m::resource::request &request)
{
	if(request.parv.size() < 1)
		throw m::NEED_MORE_PARAMS
		{
			"user_id required"
		};

	m::user::id::buf user_id
	{
		url::decode(user_id, request.parv[0])
	};

	if(request.user_id != user_id)
		throw m::UNSUPPORTED
		{
			"Deleting user data as someone else is not yet supported"
		};

	if(request.parv.size() < 2)
		throw m::NEED_MORE_PARAMS
		{
			"user command required"
		};

	const string_view &cmd
	{
		request.parv[1]
	};

	if(cmd == "rooms")
		return delete__rooms(client, request, user_id);

	throw m::NOT_FOUND
	{
		"/user command not found"
	};
}

m::resource::method
delete_method
{
	user_resource, "DELETE", delete_user,
	{
		delete_method.REQUIRES_AUTH
	}
};
