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
	"Client 7.2 :Room aliases"
};

m::resource
directory_room_resource
{
	"/chat/client/r0/directory/room/",
	{
		"(7.2) Room aliases",
		directory_room_resource.DIRECTORY
	}
};

static m::resource::response
get__directory_room(client &client,
                    const m::resource::request &request);

m::resource::method
directory_room_get
{
	directory_room_resource, "GET", get__directory_room
};

m::resource::response
get__directory_room(client &client,
                    const m::resource::request &request)
{
	m::room::alias::buf room_alias
	{
		url::decode(room_alias, request.parv[0])
	};

	const m::room::id::buf room_id
	{
		m::room_id(room_alias)
	};

	return m::resource::response
	{
		client, json::members
		{
			{ "room_id", room_id }
		}
	};
}

static m::resource::response
put__directory_room(client &client,
                    const m::resource::request &request);

m::resource::method
directory_room_put
{
	directory_room_resource, "PUT", put__directory_room,
	{
		directory_room_put.REQUIRES_AUTH
	}
};

m::resource::response
put__directory_room(client &client,
                    const m::resource::request &request)
{
	if(request.parv.size() < 1)
		throw m::NEED_MORE_PARAMS
		{
			"Room alias path parameter missing"
		};

	m::room::alias::buf room_alias
	{
		url::decode(room_alias, request.parv[0])
	};

	const m::room::id &room_id
	{
		unquote(request.at("room_id"))
	};

	if(!exists(room_id))
		throw m::NOT_FOUND
		{
			"Room %s is not found here.",
			string_view{room_id}
		};

	const unique_mutable_buffer buf
	{
		48_KiB
	};

	// Generate the new content of the m.room.aliases
	json::stack out{buf};
	{
		json::stack::object content
		{
			out
		};

		json::stack::array array
		{
			content, "aliases"
		};

		// Iterate existing aliases for host
		const m::room::aliases aliases{room_id};
		aliases.for_each(room_alias.host(), [&room_alias, &array]
		(const m::room::alias &existing_alias)
		{
			// Check for duplicate
			if(iequals(existing_alias, room_alias))
				throw m::error
				{
					http::CONFLICT, "M_EXISTS",
					"Room alias %s already exists",
					string_view{room_alias},
				};

			// Add existing alias
			array.append(existing_alias);
			return true;
		});

		//Add new alias
		array.append(room_alias);
	}

	// Commit the new event
	// TODO: ABA with another aliases event.
	const auto eid
	{
		m::send(room_id, request.user_id, "m.room.aliases", room_alias.host(), json::object
		{
			out.completed()
		})
	};

	return m::resource::response
	{
		client, json::members
		{
			{ "event_id", eid }
		}
	};
}

static m::resource::response
delete__directory_room(client &client,
                       const m::resource::request &request);

m::resource::method
directory_room_delete
{
	directory_room_resource, "DELETE", delete__directory_room,
	{
		directory_room_delete.REQUIRES_AUTH
	}
};

m::resource::response
delete__directory_room(client &client,
                       const m::resource::request &request)
{
	if(request.parv.size() < 1)
		throw m::NEED_MORE_PARAMS
		{
			"Room alias path parameter missing"
		};

	m::room::alias::buf room_alias
	{
		url::decode(room_alias, request.parv[0])
	};

	const m::room::id::buf room_id
	{
		m::room_id(room_alias)
	};

	const unique_mutable_buffer buf
	{
		48_KiB
	};

	// Generate the new content of the m.room.aliases
	json::stack out{buf};
	bool removed {false};
	{
		json::stack::object content
		{
			out
		};

		json::stack::array array
		{
			content, "aliases"
		};

		// Iterate existing aliases for host
		const m::room::aliases aliases{room_id};
		aliases.for_each(room_alias.host(), [&room_alias, &array, &removed]
		(const m::room::alias &existing_alias)
		{
			// Check for existing
			if(!iequals(existing_alias, room_alias))
				array.append(existing_alias);
			else
				removed = true;

			return true;
		});
	}

	if(!removed)
		return m::resource::response
		{
			client, http::OK
		};

	// Commit the new event
	// TODO: ABA with another aliases event.
	const auto eid
	{
		m::send(room_id, request.user_id, "m.room.aliases", room_alias.host(), json::object
		{
			out.completed()
		})
	};

	return m::resource::response
	{
		client, json::members
		{
			{ "event_id", eid }
		}
	};
}
