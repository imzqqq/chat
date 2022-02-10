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

namespace
{
	using user_devices_map = std::map<m::user::id, json::array>;
	using host_users_map = std::map<string_view, user_devices_map>;
	using query_map = std::map<string_view, m::fed::user::keys::query>;
	using failure_map = std::map<string_view, std::exception_ptr, std::less<>>;
	using buffer_list = std::vector<unique_buffer<mutable_buffer>>;
}

static host_users_map
parse_user_request(const json::object &device_keys);

static bool
send_request(const string_view &,
             const user_devices_map &,
             failure_map &,
             buffer_list &,
             query_map &);

static query_map
send_requests(const host_users_map &,
              buffer_list &,
              failure_map &);

static void
recv_response(const m::resource::request &,
              const string_view &,
              m::fed::user::keys::query &,
              failure_map &,
              json::stack::object &);

static void
recv_responses(const m::resource::request &,
               query_map &,
               failure_map &,
               json::stack::object &,
               const milliseconds &);

static void
handle_failures(const failure_map &,
                json::stack::object &);

static m::resource::response
post__keys_query(client &client,
                 const m::resource::request &request);

mapi::header
IRCD_MODULE
{
	"Client 14.11.5.2 :Key management API"
};

ircd::m::resource
query_resource
{
	"/chat/client/r0/keys/query",
	{
		"(14.11.5.2.2) Keys query",
	}
};

ircd::m::resource
query_resource__unstable
{
	"/chat/client/unstable/keys/query",
	{
		"(14.11.5.2.2) Keys query",
	}
};

m::resource::method
method_post
{
	query_resource, "POST", post__keys_query,
	{
		method_post.REQUIRES_AUTH
	}
};

m::resource::method
method_post__unstable
{
	query_resource__unstable, "POST", post__keys_query,
	{
		method_post.REQUIRES_AUTH
	}
};

conf::item<milliseconds>
query_timeout_default
{
	{ "name",     "ircd.client.keys.query.timeout.default" },
	{ "default",  10000L                                   },
};

conf::item<milliseconds>
query_timeout_min
{
	{ "name",     "ircd.client.keys.query.timeout.min" },
	{ "default",  5000L                                },
};

conf::item<milliseconds>
query_timeout_max
{
	{ "name",     "ircd.client.keys.query.timeout.max" },
	{ "default",  20000L                               },
};

conf::item<size_t>
query_limit
{
	{ "name",     "ircd.client.keys.query.limit" },
	{ "default",  4096L                          },
};

m::resource::response
post__keys_query(client &client,
                 const m::resource::request &request)
{
	const milliseconds
	timeout_min{query_timeout_min},
	timeout_max{query_timeout_max},
	timeout_default{query_timeout_default},
	timeout
	{
		std::clamp(request.get("timeout", timeout_default), timeout_min, timeout_max)
	};

	const json::string token
	{
		request["token"]
	};

	const m::event::idx since
	{
		m::sync::sequence(m::sync::make_since(token))
	};

	const json::object &request_keys
	{
		request.at("device_keys")
	};

	const host_users_map map
	{
		parse_user_request(request_keys)
	};

	buffer_list buffers;
	failure_map failures;
	query_map queries
	{
		send_requests(map, buffers, failures)
	};

	m::resource::response::chunked response
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

	recv_responses(request, queries, failures, top, timeout);
	handle_failures(failures, top);
	return {};
}

void
handle_failures(const failure_map &failures,
                json::stack::object &out)
{
	json::stack::object response_failures
	{
		out, "failures"
	};

	for(const auto &p : failures)
	{
		const string_view &hostname(p.first);
		const std::exception_ptr &eptr(p.second);
		json::stack::member
		{
			response_failures, hostname, what(eptr)
		};
	}
}

void
recv_responses(const m::resource::request &client_request,
               query_map &queries,
               failure_map &failures,
               json::stack::object &out,
               const milliseconds &timeout)
try
{
	const system_point timedout
	{
		ircd::now<system_point>() + timeout
	};

	json::stack::object response_keys
	{
		out, "device_keys"
	};

	while(!queries.empty())
	{
		static const auto dereferencer{[]
		(auto &it) -> m::fed::user::keys::query &
		{
			return it->second;
		}};

		auto next
		{
			ctx::when_any(begin(queries), end(queries), dereferencer)
		};

		next.wait_until(timedout); // throws on timeout
		const auto it{next.get()};
		const unwind remove{[&queries, &it]
		{
			queries.erase(it);
		}};

		const auto &remote(it->first);
		auto &request(it->second);

		assert(!failures.count(remote));
		if(failures.count(remote))
			continue;

		recv_response(client_request, remote, request, failures, response_keys);
	}
}
catch(const std::exception &)
{
	for(const auto &[remote, request] : queries)
		failures.emplace(remote, std::current_exception());
}

void
recv_response(const m::resource::request &client_request,
              const string_view &remote,
              m::fed::user::keys::query &request,
              failure_map &failures,
              json::stack::object &object)
try
{
	const auto code
	{
		request.get()
	};

	const json::object response
	{
		request
	};

	const json::object &device_keys
	{
		response["device_keys"]
	};

	for(const auto &[_user_id, device_keys] : device_keys)
	{
		const m::user::id &user_id
		{
			_user_id
		};

		json::stack::object user_object
		{
			object, user_id
		};

		for(const auto &[device_id, keys] : json::object(device_keys))
			json::stack::member
			{
				user_object, device_id, keys
			};
	}

	const json::object &master_keys
	{
		response["master_keys"]
	};

	for(const auto &[_user_id, master_key] : master_keys)
	{
		const m::user::id &user_id
		{
			_user_id
		};

		json::stack::member
		{
			object, user_id, json::object
			{
				master_key
			}
		};
	}

	const json::object &self_signing_keys
	{
		response["self_signing_keys"]
	};

	for(const auto &[_user_id, self_signing_key] : self_signing_keys)
	{
		const m::user::id &user_id
		{
			_user_id
		};

		json::stack::member
		{
			object, user_id, json::object
			{
				self_signing_key
			}
		};
	}

	const json::object &user_signing_keys
	{
		response["user_signing_keys"]
	};

	for(const auto &[_user_id, user_signing_key] : user_signing_keys)
	{
		const m::user::id &user_id
		{
			_user_id
		};

		if(client_request.user_id != _user_id)
			continue;

		json::stack::member
		{
			object, user_id, json::object
			{
				user_signing_key
			}
		};
	}
}
catch(const std::exception &e)
{
	log::error
	{
		m::log, "user keys query from %s :%s",
		remote,
		e.what()
	};

	failures.emplace(remote, std::current_exception());
}

query_map
send_requests(const host_users_map &hosts,
              buffer_list &buffers,
              failure_map &failures)
{
	query_map ret;
	for(const auto &pair : hosts)
	{
		const string_view &remote(pair.first);
		const user_devices_map &user_devices(pair.second);
		send_request(remote, user_devices, failures, buffers, ret);
	}

	return ret;
}

bool
send_request(const string_view &remote,
             const user_devices_map &queries,
             failure_map &failures,
             buffer_list &buffers,
             query_map &ret)
try
{
	static const size_t buffer_unit_size
	{
		m::user::id::MAX_SIZE + 1     // 256
		+ 128                         // device_id
	};

	const size_t buffer_size
	{
		8_KiB + // headers
		buffer_unit_size * std::min(queries.size(), size_t(query_limit))
	};

	const auto &buffer
	{
		buffers.emplace_back(buffer_size)
	};

	m::fed::user::keys::query::opts opts;
	opts.remote = remote;
	ret.emplace
	(
		std::piecewise_construct,
		std::forward_as_tuple(remote),
		std::forward_as_tuple(queries, buffer, std::move(opts))
	);

	return true;
}
catch(const std::exception &e)
{
	log::error
	{
		m::log, "user keys query to %s :%s",
		remote,
		e.what()
	};

	failures.emplace(remote, std::current_exception());
	return false;
}

host_users_map
parse_user_request(const json::object &device_keys)
{
	host_users_map ret;
	for(const auto &member : device_keys)
	{
		const m::user::id &user_id(member.first);
		const json::array &device_ids(member.second);
		const string_view &host(user_id.host());

		auto it(ret.lower_bound(host));
		if(it == end(ret) || it->first != host)
			it = ret.emplace_hint(it, host, user_devices_map{});

		user_devices_map &users(it->second);
		{
			auto it(users.lower_bound(user_id));
			if(it == end(users) || it->first != user_id)
				it = users.emplace_hint(it, user_id, json::array{});

			if(!empty(device_ids))
				it->second = device_ids;
		}
	}

	return ret;
}
