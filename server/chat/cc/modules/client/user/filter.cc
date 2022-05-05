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

m::resource::response
get__filter(client &client,
            const m::resource::request &request,
            const m::user::id &user_id)
{
	char filter_id_buf[64];
	const auto filter_id
	{
		url::decode(filter_id_buf, request.parv[2])
	};

	const m::user user
	{
		user_id
	};

	const m::user::filter filter
	{
		user_id
	};

	filter.get(filter_id, [&client]
	(const string_view &filter_id, const json::object &filter)
	{
		m::resource::response
		{
			client, filter
		};
	});

	// Responded from closure.
	return {};
}

// (5.2) Uploads a new filter definition to the homeserver. Returns a filter ID that
// may be used in future requests to restrict which events are returned to the client.
m::resource::response
post__filter(client &client,
             const m::resource::request::object<const m::filter> &request,
             const m::user::id &user_id)
{
	// (5.2) Required. The id of the user uploading the filter. The access
	// token must be authorized to make requests for this user id.
	if(user_id != request.user_id)
		throw m::FORBIDDEN
		{
			"Trying to post a filter for `%s' but you are `%s'",
			user_id,
			request.user_id
		};

	// (5.2) List of event fields to include. If this list is absent then all fields are
	// included. The entries may include '.' charaters to indicate sub-fields. So
	// ['content.body'] will include the 'body' field of the 'content' object. A literal '.'
	// character in a field name may be escaped using a '\'. A server may include more
	// fields than were requested.
	const auto &event_fields
	{
		json::get<"event_fields"_>(request)
	};

	// (5.2) The format to use for events. 'client' will return the events in a format suitable
	// for clients. 'federation' will return the raw event as receieved over federation.
	// The default is 'client'. One of: ["client", "federation"]
	const auto &event_format
	{
		json::get<"event_format"_>(request)
	};

	// (5.2) The user account data that isn't associated with rooms to include.
	const auto &account_data
	{
		json::get<"account_data"_>(request)
	};

	// (5.2) Filters to be applied to room data.
	const auto &room
	{
		json::get<"room"_>(request)
	};

	const auto &state
	{
		json::get<"state"_>(room)
	};

	const auto &presence
	{
		// (5.2) The presence updates to include.
		json::get<"presence"_>(request)
	};

	const m::user user
	{
		user_id
	};

	const m::user::filter filter
	{
		user
	};

	char filter_id_buf[m::id::MAX_SIZE];
	const auto filter_id
	{
		filter.set(filter_id_buf, request.body)
	};

	return m::resource::response
	{
		client, http::CREATED,
		{
			{ "filter_id", filter_id }
		}
	};
}
