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
	"Client 11.6 :Presence"
};

m::resource
presence_resource
{
	"/chat/client/r0/presence/",
	{
		"(11.6.2) Presence",
		resource::DIRECTORY,
	}
};

//
// get
//

static m::resource::response
get__presence(client &,
              const m::resource::request &);

m::resource::method
method_get
{
	presence_resource, "GET", get__presence
};

static resource::response
get__presence_status(client &,
                     const m::resource::request &,
                     const m::user::id &);

resource::response
get__presence(client &client,
              const m::resource::request &request)
{
	if(request.parv.size() < 1)
		throw m::NEED_MORE_PARAMS
		{
			"user_id or command required"
		};

	m::user::id::buf user_id
	{
		url::decode(user_id, request.parv[0])
	};

	if(request.parv.size() < 2)
		throw m::NEED_MORE_PARAMS
		{
			"command required"
		};

	const auto &cmd
	{
		request.parv[1]
	};

	if(cmd == "status")
		return get__presence_status(client, request, user_id);

	throw m::NOT_FOUND
	{
		"Presence command not found"
	};
}

resource::response
get__presence_status(client &client,
                     const m::resource::request &request,
                     const m::user::id &user_id)
{
	const m::user user
	{
		user_id
	};

	m::presence::get(user, [&client]
	(const json::object &object)
	{
		resource::response
		{
			client, object
		};
	});

	return {}; // responded from closure or threw
}

//
// POST ?
//

static m::resource::response
post__presence(client &,
               const m::resource::request &);

m::resource::method
method_post
{
	presence_resource, "POST", post__presence,
	{
		method_post.REQUIRES_AUTH
	}
};

m::resource::response
post__presence(client &client,
               const m::resource::request &request)
{
	if(request.parv.size() < 1)
		throw m::NEED_MORE_PARAMS
		{
			"command required"
		};

	throw m::NOT_FOUND
	{
		"Presence command not found"
	};
}

//
// put
//

static m::resource::response
put__presence_status(client &,
                     const m::resource::request &,
                     const m::user::id &);

static m::resource::response
put__presence(client &,
              const m::resource::request &);

m::resource::method
method_put
{
	presence_resource, "PUT", put__presence,
	{
		method_put.REQUIRES_AUTH
	}
};

m::resource::response
put__presence(client &client,
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

	if(user_id != request.user_id)
		throw m::FORBIDDEN
		{
			"You cannot set the presence of '%s' when you are '%s'",
			user_id,
			request.user_id
		};

	if(request.parv.size() < 2)
		throw m::NEED_MORE_PARAMS
		{
			"command required"
		};

	const auto &cmd
	{
		request.parv[1]
	};

	if(cmd == "status")
		return put__presence_status(client, request, user_id);

	throw m::NOT_FOUND
	{
		"Presence command not found"
	};
}

m::resource::response
put__presence_status(client &client,
                     const m::resource::request &request,
                     const m::user::id &user_id)
{
	const string_view &presence
	{
		unquote(request.at("presence"))
	};

	if(!m::presence::valid_state(presence))
		throw m::UNSUPPORTED
		{
			"That presence state is not supported"
		};

	const string_view &status_msg
	{
		trunc(unquote(request["status_msg"]), 390)
	};

	const m::user user
	{
		request.user_id
	};

	bool modified{true};
	m::presence::get(std::nothrow, user, [&modified, &presence, &status_msg]
	(const json::object &object)
	{
		if(unquote(object.get("presence")) != presence)
			return;

		if(unquote(object.get("status_msg")) != status_msg)
			return;

		modified = false;
	});

	if(!modified)
		return m::resource::response
		{
			client, http::OK
		};

	const auto eid
	{
		m::presence::set(user, presence, status_msg)
	};

	return m::resource::response
	{
		client, http::OK
	};
}
