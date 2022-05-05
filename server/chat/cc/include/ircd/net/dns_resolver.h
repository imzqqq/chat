// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

//
// This file is not part of the ircd.h precompiled header because it contains
// boost::asio assets which we cannot forward declare. It is part of the
// ircd/asio.h precompiled header.
//

namespace ircd::net::dns
{
	struct tag;
	struct resolver extern *resolver_instance;

	using answers = vector_view<const rfc1035::answer>;
	using answers_callback = std::function<void (std::exception_ptr, const tag &, const answers &)>;

	constexpr const size_t MAX_COUNT {64};

	uint16_t resolver_call(const hostport &, const opts &);
}

struct ircd::net::dns::resolver
{
	using header = rfc1035::header;

	static conf::item<std::string> servers;
	static conf::item<milliseconds> timeout;
	static conf::item<milliseconds> send_rate;
	static conf::item<size_t> send_burst;
	static conf::item<size_t> retry_max;

	answers_callback callback;
	std::vector<ip::udp::endpoint> server;       // The list of active servers
	size_t server_next{0};                       // Round-robin state to hit servers
	ctx::dock dock, done;
	ctx::mutex mutex;
	std::map<uint16_t, tag> tags;                // The active requests
	steady_point send_last;                      // Time of last send
	std::deque<uint16_t> sendq;                  // Queue of frames for rate-limiting
	ip::udp::socket ns;                          // A pollable activity object
	bool recv_idle {false};                      // Timeout worker won't run if false

	// util
	void add_server(const ipport &);
	void add_server(const string_view &);
	void set_servers(const string_view &list);
	void set_servers();

	// removal (must have lock)
	void unqueue(tag &);
	void remove(tag &);
	decltype(tags)::iterator remove(tag &, const decltype(tags)::iterator &);
	void error_one(tag &, const std::exception_ptr &, const bool &remove = true);
	void error_one(tag &, const std::system_error &, const bool &remove = true);
	void error_all(const std::error_code &, const bool &remove = true);
	void cancel_all(const bool &remove = true);

	// reception
	bool handle_error(const header &, tag &);
	void handle_reply(const header &, const const_buffer &body, tag &);
	void handle_reply(const ipport &, const header &, const const_buffer &body);
	void handle(const ipport &, const mutable_buffer &);
	void handle_interrupt(ctx::ctx *const &) noexcept;
	std::tuple<net::ipport, mutable_buffer> recv_recv(const mutable_buffer &);
	void recv_worker();
	ctx::context recv_context;

	// submission
	void send_query(const ip::udp::endpoint &, tag &);
	void queue_query(tag &);
	void send_query(tag &);
	void submit(tag &);

	// timeout
	bool check_timeout(const uint16_t &id, tag &, const steady_point &expired);
	void check_timeouts(const milliseconds &timeout);
	void timeout_worker();
	ctx::context timeout_context;

	// sendq
	void flush(const uint16_t &);
	void sendq_work();
	void sendq_clear();
	void sendq_worker();
	ctx::context sendq_context;

	template<class... A> tag &set_tag(A&&...);
	const_buffer make_query(const mutable_buffer &buf, tag &);
	uint16_t operator()(const hostport &, const opts &);

	resolver(answers_callback);
	~resolver() noexcept;
};

struct ircd::net::dns::tag
{
	uint16_t id {0};
	hostport hp;
	dns::opts opts;       // note: invalid after query sent
	const_buffer question;
	steady_point last {steady_point::min()};
	uint8_t tries {0};
	uint rcode {0};
	ipport server;
	char hostbuf[rfc1035::NAME_BUFSIZE];
	char servicebuf[256];
	char qbuf[512];

	tag(const hostport &hp, const dns::opts &opts);
	tag(tag &&) = delete;
	tag(const tag &) = delete;
	tag &operator=(tag &&) = delete;
	tag &operator=(const tag &) = delete;
};

inline
ircd::net::dns::tag::tag(const hostport &hp,
                         const dns::opts &opts)
:hp{hp}
,opts{opts}
{
	this->hp.host =
	{
		hostbuf, copy(hostbuf, hp.host)
	};

	this->hp.service =
	{
		servicebuf, copy(servicebuf, hp.service)
	};
}
