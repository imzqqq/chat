// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

namespace ircd::m::client_capabilities
{
	static resource::response get(client &, const resource::request &);

	extern resource::method method_get;
	extern m::resource resource;
}

ircd::mapi::header
IRCD_MODULE
{
	"Client 6 :Capabilities Negotiation"
};

decltype(ircd::m::client_capabilities::resource)
ircd::m::client_capabilities::resource
{
	"/chat/client/r0/capabilities",
	{
		"(6.1) Gets information about the server's supported feature "
		"set and other relevant capabilities. ",
	}
};

decltype(ircd::m::client_capabilities::method_get)
ircd::m::client_capabilities::method_get
{
	resource, "GET", get,
	{
		method_get.REQUIRES_AUTH
		| method_get.RATE_LIMITED
	}
};

ircd::resource::response
ircd::m::client_capabilities::get(client &client,
                                  const resource::request &request)
{
	const bool m_change_password__enabled
	{
		mods::loaded("client_account")
	};

	const json::value default_room_version
	{
		string_view{m::createroom::version_default}, json::STRING
	};

	return resource::response
	{
		client, json::members
		{
			{ "capabilities", json::members
			{
				{ "m.change_password", json::members
				{
					{ "enabled", m_change_password__enabled },
				}},
				{ "m.room_versions", json::members
				{
					{ "default", default_room_version },
					{ "available", json::members
					{
						{ "1", "stable"  },
						{ "2", "stable"  },
						{ "3", "stable"  },
						{ "4", "stable"  },
						{ "5", "stable"  },
						{ "6", "stable"  },
					}},
				}},
			}},
		},
	};
}
