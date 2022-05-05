// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2019 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

using namespace ircd;

// federation_invite (weak)
extern conf::item<milliseconds>
stream_cross_sleeptime;

static void
process(client &,
        const m::resource::request &,
        const m::event &);

static m::resource::response
put__invite(client &client,
            const m::resource::request &request);

mapi::header
IRCD_MODULE
{
	"Federation 12 :Inviting to a room (v2)"
};

m::resource
invite_resource
{
	"/chat/federation/v2/invite/",
	{
		"Inviting to a room",
		resource::DIRECTORY
	}
};

m::resource::method
method_put
{
	invite_resource, "PUT", put__invite,
	{
		method_put.VERIFY_ORIGIN
	}
};

m::resource::response
put__invite(client &client,
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

	if(request.parv.size() < 2)
		throw m::NEED_MORE_PARAMS
		{
			"event_id path parameter required"
		};

	m::event::id::buf event_id
	{
		url::decode(event_id, request.parv[1])
	};

	const json::string &room_version
	{
		request.get("room_version", "1")
	};

	m::event event
	{
		request["event"], event_id
	};

	if(!json::get<"event_id"_>(event))
		if(room_version == "1" || room_version == "2")
			json::get<"event_id"_>(event) = event_id;

	if(!check_id(event, room_version))
		throw m::BAD_REQUEST
		{
			"Claimed event_id %s is incorrect.",
			string_view{event_id},
		};

	if(at<"room_id"_>(event) != room_id)
		throw m::error
		{
			http::NOT_MODIFIED, "M_MISMATCH_ROOM_ID",
			"ID of room in request body %s does not match path param %s",
			string_view{at<"room_id"_>(event)},
			string_view{room_id},
		};

	if(at<"type"_>(event) != "m.room.member")
		throw m::error
		{
			http::NOT_MODIFIED, "M_INVALID_TYPE",
			"event.type must be m.room.member"
		};

	if(unquote(at<"content"_>(event).at("membership")) != "invite")
		throw m::error
		{
			http::NOT_MODIFIED, "M_INVALID_CONTENT_MEMBERSHIP",
			"event.content.membership must be invite."
		};

	if(at<"origin"_>(event) != request.node_id)
		throw m::error
		{
			http::FORBIDDEN, "M_INVALID_ORIGIN",
			"event.origin must be you."
		};

	const m::user::id &sender
	{
		at<"sender"_>(event)
	};

	if(sender.host() != request.node_id)
		throw m::error
		{
			http::FORBIDDEN, "M_INVALID_ORIGIN",
			"event.sender must be your user."
		};

	const m::user::id &target
	{
		at<"state_key"_>(event)
	};

	if(!my_host(target.host()))
		throw m::error
		{
			http::FORBIDDEN, "M_INVALID_STATE_KEY",
			"event.state_key must be my user."
		};

	m::event::conforms non_conforms;
	const m::event::conforms report
	{
		event, non_conforms.report
	};

	if(!report.clean())
		throw m::error
		{
			http::NOT_MODIFIED, "M_INVALID_EVENT",
			"Proffered event has the following problems: %s",
			string(report)
		};

	// May conduct disk IO to check ACL
	if(m::room::server_acl::enable_write)
		if(!m::room::server_acl::check(room_id, request.node_id))
			throw m::ACCESS_DENIED
			{
				"You are not permitted by the room's server access control list."
			};

	// May conduct network IO to fetch node's key; disk IO to fetch node's key
	if(!verify(event, request.node_id))
		throw m::ACCESS_DENIED
		{
			"Invite event fails verification for %s",
			string_view{request.node_id},
		};

	thread_local char sigs[4_KiB];
	m::event signed_event
	{
		signatures(sigs, event, target.host())
	};

	signed_event.event_id = event_id;
	const json::strung signed_json
	{
		signed_event
	};

	// Send back the signed event first before eval. If we eval the signed
	// event first: the effects will occur before the inviting server has
	// the signed event returned from us; they might not consider the user
	// invited yet, causing trouble for the eval effects. That may actually
	// still happen due to the two separate TCP connections being uncoordinated
	// (one for this request, and another when m::eval effects connect to them
	// and make any requests). But either way if this call fails then we will
	// lose the invite but that may not be such a bad thing.
	m::resource::response response
	{
		client, json::members
		{
			{ "event", json::object{signed_json} }
		}
	};

	// Synapse needs time to process our response otherwise our eval below may
	// complete before this response arrives for them and is processed.
	ctx::sleep(milliseconds(stream_cross_sleeptime));

	// Post processing, does not throw.
	process(client, request, signed_event);

	// note: returning a resource response is a symbolic/indicator action to
	// the caller and has no real effect at the point of return.
	return response;
}

void
process(client &client,
        const m::resource::request &request,
        const m::event &event)
try
{
	// Eval the dual-signed invite event. This will write it locally. This will
	// also try to sync the room as best as possible. The invitee will then be
	// presented with this invite request in their rooms list.
	m::vm::opts vmopts;
	vmopts.node_id = request.node_id;

	// Synapse may 403 a fetch of the prev_event of the invite event.
	vmopts.phase.set(m::vm::phase::FETCH_PREV, false);
	vmopts.phase.set(m::vm::phase::EMPTION, false);

	// Don't throw an exception for a re-evaluation; this will just be a no-op
	vmopts.nothrows |= m::vm::fault::EXISTS;
	vmopts.room_version = unquote(request.get("room_version", "1"));

	m::vm::eval
	{
		event, vmopts
	};
}
catch(const std::exception &e)
{
	log::error
	{
		m::log, "Processing invite from:%s to:%s :%s",
		json::get<"sender"_>(event),
		json::get<"state_key"_>(event),
		e.what(),
	};

	return;
}
