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

extern "C" std::list<net::listener> listeners;

extern "C" bool loaded_listener(const string_view &name);
static bool load_listener(const string_view &, const json::object &);
static bool load_listener(const m::event &);
extern "C" bool unload_listener(const string_view &name);
extern "C" bool load_listener(const string_view &name);
static void init_listeners();
static void on_quit() noexcept;
static void on_run();
static void on_unload();
static void on_load();

mapi::header
IRCD_MODULE
{
	"Server listeners", on_load, on_unload
};

const ircd::run::changed
_on_change{[](const auto &level)
{
	if(level == run::level::RUN)
		on_run();
	else if(level == run::level::QUIT)
		on_quit();
}};

/// Active listener state
decltype(listeners)
listeners;

//
// On module load any existing listener descriptions are sought out
// of room state and instantiated (i.e on startup).
//

void
on_load()
{
	if(!bool(ircd::net::listen))
	{
		log::warning
		{
			"Not listening on any addresses because nolisten flag is set."
		};

		return;
	}

	init_listeners();
}

void
on_unload()
{
	log::debug
	{
		"Clearing %zu listeners...",
		listeners.size()
	};

	listeners.clear();
}

void
on_run()
{
	for(auto &listener : listeners)
		start(listener);

	if(!listeners.empty())
		log::notice
		{
			"Accepting connections on %zu listeners...",
			listeners.size()
		};
}

void
on_quit()
noexcept
{
	for(auto &listener : listeners)
		stop(listener);

	if(!listeners.empty())
		log::notice
		{
			"Stopped accepting new connections on %zu listeners",
			listeners.size()
		};
}

void
init_listeners()
{
	const m::room::id::buf my_room
	{
		"ircd", m::origin(m::my())
	};

	const m::room::state state
	{
		my_room
	};

	state.for_each("ircd.listen", [](const m::event &event)
	{
		load_listener(event);
	});

	if(listeners.empty())
		log::warning
		{
			"No listening sockets configured; can't hear anyone."
		};
}

//
// Upon processing of a new event which saved a listener description
// to room state in its content, we instantiate the listener here.
//

static void
create_listener(const m::event &event,
                m::vm::eval &)
{
	load_listener(event);
}

/// Hook for a new listener description being sent.
m::hookfn<m::vm::eval &>
create_listener_hook
{
	create_listener,
	{
		{ "_site",       "vm.effect"    },
		{ "room_id",     "!ircd"        },
		{ "type",        "ircd.listen"  },
	}
};

//
// Common
//

bool
load_listener(const string_view &name)
try
{
	bool ret{false};
	const m::room::id::buf my_room
	{
		"ircd", m::origin(m::my())
	};

	const m::room::state state
	{
		my_room
	};

	state.get("ircd.listen", name, [&ret]
	(const m::event &event)
	{
		ret = load_listener(event);
	});

	return ret;
}
catch(const m::NOT_FOUND &e)
{
	log::error
	{
		"Failed to find any listener configuration for '%s'",
		name
	};

	return false;
}

bool
unload_listener(const string_view &name)
{
	if(!loaded_listener(name))
		return false;

	listeners.remove_if([&name]
	(const auto &listener)
	{
		return listener.name() == name;
	});

	return true;
}

bool
load_listener(const m::event &event)
{
	const string_view &name
	{
		at<"state_key"_>(event)
	};

	const json::object &opts
	{
		json::get<"content"_>(event)
	};

	if(!load_listener(name, opts))
		return false;

	log::notice
	{
		"Listener '%s' configured for %s:%s by %s",
		name,
		opts["host"],
		opts["port"],
		string_view{event.event_id},
	};

	return true;
}

ctx::context
_listener_allow
{
	"listener allow", 64_KiB, context::POST, []
	{
		while(1)
		{
			client::dock.wait([]
			{
				return !client::pool.avail();
			});

			client::dock.wait([]
			{
				if(!client::pool.avail())
					return false;

				if(client::map.size() >= size_t(client::settings::max_client))
					return false;

				return true;
			});

			for(auto &listener : listeners)
				allow(listener);
		}
	}
};

static bool
_listener_proffer(net::listener &listener,
                  const net::ipport &ipport)
{
	thread_local char strbuf[256];
	if(unlikely(ircd::run::level != ircd::run::level::RUN))
	{
		log::dwarning
		{
			"Refusing to add new client from %s in runlevel %s",
			string(strbuf, ipport),
			reflect(ircd::run::level)
		};

		return false;
	}

	// Sets the asynchronous handler for the next accept. We can play with
	// delaying this call under certain conditions to provide flow control.
	allow(listener);

	if(unlikely(client::map.size() >= size_t(client::settings::max_client)))
	{
		log::warning
		{
			"Refusing to add new client from %s because maximum of %zu reached",
			string(strbuf, ipport),
			size_t(client::settings::max_client)
		};

		return false;
	}

	if(unlikely(!client::pool.avail()))
	{
		log::dwarning
		{
			"Refusing to add new client from %s because request pool exhausted.",
			string(strbuf, ipport),
		};

		return false;
	}

	if(client::count(ipport) >= size_t(client::settings::max_client_per_peer))
	{
		log::dwarning
		{
			"Refusing to add new client from %s: maximum of %zu connections for peer.",
			string(ipport),
			size_t(client::settings::max_client_per_peer)
		};

		return false;
	}

	return true;
}

bool
load_listener(const string_view &name,
              const json::object &opts)
try
{
	if(loaded_listener(name))
		throw error
		{
			"A listener with the name '%s' is already loaded", name
		};

	listeners.emplace_back(name, opts, client::create, _listener_proffer);

	if(ircd::run::level == ircd::run::level::RUN)
		start(listeners.back());

	return true;
}
catch(const std::exception &e)
{
	log::error
	{
		"Failed to init listener '%s' :%s",
		name,
		e.what()
	};

	return false;
}

bool
loaded_listener(const string_view &name)
{
	return end(listeners) != std::find_if(begin(listeners), end(listeners), [&name]
	(const auto &listener)
	{
		return listener.name() == name;
	});
}
