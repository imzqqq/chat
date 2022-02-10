// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

namespace ircd::m
{
	using namespace ircd::spirit;

	[[noreturn]] void failure(const qi::expectation_failure<const char *> &, const string_view &);
}

struct __attribute__((visibility("hidden"))) ircd::m::id::input
:qi::grammar<const char *, unused_type>
{
	using id = m::id;
	using it = const char *;
	template<class R = unused_type, class... S> using rule = qi::rule<it, R, S...>;

	// Sigils
	const rule<id::sigil> event_id_sigil         { lit(char(id::EVENT))          ,"event_id sigil" };
	const rule<id::sigil> user_id_sigil          { lit(char(id::USER))            ,"user_id sigil" };
	const rule<id::sigil> room_id_sigil          { lit(char(id::ROOM))            ,"room_id sigil" };
	const rule<id::sigil> room_alias_sigil       { lit(char(id::ROOM_ALIAS))   ,"room_alias sigil" };
	const rule<id::sigil> group_id_sigil         { lit(char(id::GROUP))          ,"group_id sigil" };
	const rule<id::sigil> device_sigil           { lit(char(id::DEVICE))           ,"device sigil" };
	const rule<id::sigil> node_sigil             { lit(char(id::NODE))               ,"node sigil" };
	const rule<id::sigil> sigil
	{
		event_id_sigil    |
		user_id_sigil     |
		room_id_sigil     |
		room_alias_sigil  |
		group_id_sigil    |
		device_sigil      |
		node_sigil
		,"sigil"
	};

	// character of a localpart; must not contain ':' because that's the terminator
	const rule<> localpart_char
	{
		char_ - ':'
		,"localpart character"
	};

	// a localpart is zero or more localpart characters
	const rule<> localpart
	{
		*localpart_char
		,"localpart"
	};

	// character of a non-historical user_id localpart
	const rule<> non_historical_user_id_char
	{
		char_('\x21', '\x39') | char_('\x3B', '\x7E')
		,"user_id character"
	};

	// character of a non-historical'ish user_id localpart
	const rule<> user_id_char
	{
		char_('\x21', '\x39') | char_('\x3B', '\x7E') | char_('\x80', '\xFF')
		,"user_id character"
	};

	// a user_id localpart is 1 or more user_id localpart characters
	const rule<> user_id_localpart
	{
		+user_id_char
		,"user_id localpart"
	};

	// a prefix is a sigil and a localpart; user_id prefix
	const rule<> user_id_prefix
	{
		user_id_sigil >> user_id_localpart
		,"user_id prefix"
	};

	// a prefix is a sigil and a localpart; proper invert of user_id prefix
	const rule<> non_user_id_prefix
	{
		((!user_id_sigil) > sigil) >> localpart
		,"non user_id prefix"
	};

	// a prefix is a sigil and a localpart
	const rule<> prefix
	{
		user_id_prefix | non_user_id_prefix
		,"prefix"
	};

	// character of a v3 event_id
	const rule<> event_id_v3_char
	{
		char_("A-Za-z0-9+/") // base64 alphabet
		,"event_id version 3 character"
	};

	// fully qualified v3 event_id
	const rule<> event_id_v3
	{
		event_id_sigil >> repeat(43)[event_id_v3_char]
		,"event_id version 3"
	};

	// character of a v4 event_id
	const rule<> event_id_v4_char
	{
		char_("A-Za-z0-9_-") // base64 url-safe alphabet
		,"event_id version 4 character"
	};

	// fully qualified v4 event_id
	const rule<> event_id_v4
	{
		event_id_sigil >> repeat(43)[event_id_v4_char]
		,"event_id version 4"
	};

	/// (Appendix 4.1) Server Name
	/// A homeserver is uniquely identified by its server name. This value
	/// is used in a number of identifiers, as described below. The server
	/// name represents the address at which the homeserver in question can
	/// be reached by other homeservers. The complete grammar is:
	/// `server_name = dns_name [ ":" port]`
	/// `dns_name = host`
	/// `port = *DIGIT`
	/// where host is as defined by RFC3986, section 3.2.2. Examples of valid
	/// server names are:
	/// `matrix.org`
	/// `matrix.org:8888`
	/// `1.2.3.4` (IPv4 literal)
	/// `1.2.3.4:1234` (IPv4 literal with explicit port)
	/// `[1234:5678::abcd]` (IPv6 literal)
	/// `[1234:5678::abcd]:5678` (IPv6 literal with explicit port)
	const rule<> server_name
	{
		rfc3986::parser::remote
		,"server name"
	};

	const rule<> mxid
	{
		(prefix >> ':' >> server_name)
		| event_id_v4
		| event_id_v3
		,"mxid"
	};

	input()
	:input::base_type{rule<>{}}
	{}
};

struct __attribute__((visibility("hidden"))) ircd::m::id::output
:karma::grammar<char *, unused_type>
{
	using it = char *;
	template<class T = unused_type> using rule = karma::rule<it, T>;

	output()
	:output::base_type{rule<>{}}
	{}
};

struct __attribute__((visibility("hidden"))) ircd::m::id::parser
:input
{
	string_view operator()(const id::sigil &, const string_view &id) const;
	string_view operator()(const string_view &id) const;
}
const ircd::m::id::parser;

ircd::string_view
ircd::m::id::parser::operator()(const id::sigil &sigil,
                                const string_view &id)
const try
{
	const rule<> sigil_type
	{
		&lit(char(sigil))
		,"sigil type"
	};

	const rule<string_view> view_mxid
	{
		raw[eps > (sigil_type > mxid)]
	};

	string_view out;
	const char *start{id.begin()};
	const char *const stop
	{
		std::min(id.end(), start + MAX_SIZE)
	};

	const bool res{qi::parse(start, stop, view_mxid, out)};
	assert(res == true);
	return out;
}
catch(const qi::expectation_failure<const char *> &e)
{
	failure(e, reflect(sigil));
}

ircd::string_view
ircd::m::id::parser::operator()(const string_view &id)
const try
{
	static const rule<string_view> view_mxid
	{
		raw[eps > mxid]
	};

	string_view out;
	const char *start{id.begin()};
	const char *const stop
	{
		std::min(id.end(), start + MAX_SIZE)
	};

	const bool res(qi::parse(start, stop, view_mxid, out));
	assert(res == true);
	return out;
}
catch(const qi::expectation_failure<const char *> &e)
{
	failure(e, "mxid");
}

//
// valid
//

decltype(ircd::m::id::valid)
ircd::m::id::valid
{};

void
ircd::m::id::valid::operator()(const string_view &id)
const try
{
	const char *start{id.begin()};
	const char *const stop
	{
		std::min(id.end(), start + MAX_SIZE)
	};

	const bool ret(qi::parse(start, stop, eps > parser.mxid));
	assert(ret == true);
}
catch(const qi::expectation_failure<const char *> &e)
{
	failure(e, "mxid");
}

bool
ircd::m::id::valid::operator()(std::nothrow_t,
                               const string_view &id)
const noexcept
{
	const char *start{id.begin()};
	const char *const stop
	{
		std::min(id.end(), start + MAX_SIZE)
	};

	return qi::parse(start, stop, parser.mxid >> eoi);
}

void
ircd::m::id::valid::operator()(const id::sigil &sigil,
                               const string_view &id)
const try
{
	const parser::rule<> sigil_type
	{
		&lit(char(sigil))
		,"sigil type"
	};

	const parser::rule<> valid_mxid
	{
		eps > (sigil_type > parser.mxid)
	};

	const char *start{id.begin()};
	const char *const stop
	{
		std::min(id.end(), start + MAX_SIZE)
	};

	const bool ret(qi::parse(start, stop, valid_mxid));
	assert(ret == true);
}
catch(const qi::expectation_failure<const char *> &e)
{
	failure(e, reflect(sigil));
}

bool
ircd::m::id::valid::operator()(std::nothrow_t,
                               const id::sigil &sigil,
                               const string_view &id)
const noexcept try
{
	const parser::rule<> sigil_type
	{
		&lit(char(sigil))
		,"sigil type"
	};

	const parser::rule<> valid_mxid
	{
		(sigil_type > parser.mxid) >> eoi
	};

	const char *start{id.begin()};
	const char *const stop
	{
		std::min(id.end(), start + MAX_SIZE)
	};

	return qi::parse(start, stop, valid_mxid);
}
catch(...)
{
	return false;
}

//
// printer
//

//TODO: abstract this pattern with ircd::json::printer in ircd/spirit.h
struct __attribute__((visibility("hidden"))) ircd::m::id::printer
:output
{
	template<class generator,
	         class attribute>
	bool operator()(char *&out, char *const &stop, generator&&, attribute&&) const;

	template<class generator>
	bool operator()(char *&out, char *const &stop, generator&&) const;

	template<class... args>
	bool operator()(mutable_buffer &out, args&&... a) const;
}
const ircd::m::id::printer;

template<class gen,
         class attr>
bool
ircd::m::id::printer::operator()(char *&out,
                                 char *const &stop,
                                 gen&& g,
                                 attr&& a)
const
{
	const auto throws{[&out, &stop]
	{
		throw INVALID_MXID
		{
			"Failed to print attribute '%s' generator '%s' (%zd bytes in buffer)",
			demangle<decltype(a)>(),
			demangle<decltype(g)>(),
			size_t(stop - out)
		};
	}};

	mutable_buffer buf
	{
		out, stop
	};

	const auto gg
	{
		std::forward<decltype(g)>(g) | eps[throws]
	};

	const auto ret
	{
		generate(buf, gg, std::forward<decltype(a)>(a))
	};

	out = buffer::data(buf);
	return ret;
}

template<class gen>
bool
ircd::m::id::printer::operator()(char *&out,
                                 char *const &stop,
                                 gen&& g)
const
{
	const auto throws{[&out, &stop]
	{
		throw INVALID_MXID
		{
			"Failed to print generator '%s' (%zd bytes in buffer)",
			demangle<decltype(g)>(),
			size_t(stop - out)
		};
	}};

	mutable_buffer buf
	{
		out, stop
	};

	const auto gg
	{
		std::forward<decltype(g)>(g) | eps[throws]
	};

	const auto ret
	{
		generate(buf, gg)
	};

	out = buffer::data(buf);
	return ret;
}

template<class... args>
bool
ircd::m::id::printer::operator()(mutable_buffer &out,
                                 args&&... a)
const
{
	return operator()(buffer::begin(out), buffer::end(out), std::forward<args>(a)...);
}

//
// id::id
//

ircd::m::id::id(const string_view &str)
:id
{
	m::sigil(str), str
}
{
}

ircd::m::id::id(const id::sigil &sigil,
                const string_view &id)
:string_view
{
	parser(sigil, id)
}
{
}

ircd::m::id::id(const enum sigil &sigil,
                const mutable_buffer &buf,
                const string_view &local,
                const string_view &host)
:string_view{[&sigil, &buf, &local, &host]
{
	thread_local char tmp alignas(16) [2][MAX_SIZE];
	static_assert(sizeof(tmp) == MAX_SIZE * 2);
	const string_view src
	{
		startswith(local, sigil)?
			fmt::sprintf
			{
				buf, "%s%s%s",
				sigil == sigil::ROOM_ALIAS?
					tolower(tmp[0], local):
					local,
				host?
					":"_sv:
					string_view{},
				tolower(tmp[1], host),
			}
		:
			fmt::sprintf
			{
				buf, "%c%s%s%s",
				char(sigil),
				sigil == sigil::ROOM_ALIAS?
					tolower(tmp[0], local):
					local,
				host?
					":"_sv:
					string_view{},
				tolower(tmp[1], host),
			}
	};

	return parser(sigil, src);
}()}
{
}

ircd::m::id::id(const id::sigil &sigil,
                const mutable_buffer &buf,
                const string_view &id)
:string_view{[&sigil, &buf, &id]
{
	const string_view src
	{
		sigil == sigil::ROOM_ALIAS?
			tolower(buf, id):               //XXX no null here
		buffer::data(buf) != id.data()?
			strlcpy(buf, id):
			id
	};

	return parser(sigil, src);
}()}
{
}

ircd::m::id::id(const enum sigil &sigil,
                const mutable_buffer &buf,
                const generate_t &,
                const string_view &host)
:string_view{[&]
{
	//TODO: output grammar

	thread_local char namebuf[MAX_SIZE];
	string_view name; switch(sigil)
	{
		case sigil::USER:
			name = fmt::sprintf
			{
				namebuf, "guest%lu", rand::integer()
			};
			break;

		case sigil::ROOM_ALIAS:
			name = fmt::sprintf
			{
				namebuf, "%lu", rand::integer()
			};
			break;

		case sigil::ROOM:
		{
			static const auto &dict{rand::dict::alnum};
			const mutable_buffer dst{namebuf, 18};
			name = rand::string(dst, dict);
			break;
		}

		case sigil::DEVICE:
		{
			static const auto &dict{rand::dict::alnum};
			const mutable_buffer dst{namebuf, 10};
			name = rand::string(dst, dict);
			break;
		}

		default:
			name = fmt::sprintf
			{
				namebuf, "%c%lu", rand::character(), rand::integer()
			};
			break;
	};

	return fmt::sprintf
	{
		buf, "%c%s:%s", char(sigil), name, host
	};
}()}
{
}

ircd::string_view
ircd::m::id::swap(const mutable_buffer &buf)
const
{
	return swap(*this, buf);
}

ircd::string_view
ircd::m::id::swap(const id &id,
                  const mutable_buffer &buf_)
{
	using buffer::consume;
	using buffer::copy;
	using buffer::data;

	mutable_buffer buf(buf_);
	consume(buf, copy(buf, id.host()));
	consume(buf, copy(buf, id.local()));
	return { data(buf_), data(buf) };
}

ircd::m::id
ircd::m::id::unswap(const string_view &str,
                    const mutable_buffer &buf)
{
	size_t i(0);
	for(; i < str.size(); ++i)
		if(is_sigil(str[i]))
			break;

	if(unlikely(!i || i >= str.size()))
		throw m::INVALID_MXID
		{
			"Failed to reconstruct any MXID out of '%s'", str
		};

	return id
	{
		sigil(str[i]), buf, str.substr(i), str.substr(0, i)
	};
}

bool
ircd::m::id::literal()
const
{
	static const parser::rule<string_view> rule
	{
		rfc3986::parser::ip4_literal |
		rfc3986::parser::ip6_literal
	};

	const auto &hostname
	{
		this->hostname()
	};

	const char *start(hostname.begin()), *const stop(hostname.end());
	return qi::parse(start, stop, rule);
}

uint16_t
ircd::m::id::port()
const
{
	static const auto &host{rfc3986::parser::host};
	static const auto &port{rfc3986::parser::port};
	static const parser::rule<uint16_t> rule
	{
		omit[parser.prefix >> ':' >> host >> ':'] >> port
	};

	uint16_t ret{0};
	auto *start{begin()};
	const auto res
	{
		qi::parse(start, end(), rule, ret)
	};

	assert(res || ret == 0);
	return ret;
}

ircd::string_view
ircd::m::id::hostname()
const
{
	static const parser::rule<string_view> host
	{
		rfc3986::parser::host
	};

	static const parser::rule<string_view> rule
	{
		omit[parser.prefix >> ':'] >> raw[host]
	};

	string_view ret;
	auto *start{begin()};
	const auto res
	{
		qi::parse(start, end(), rule, ret)
	};

	assert(res || event::v4::is(*this) || event::v3::is(*this));
	assert(!res || !ret.empty());
	return ret;
}

ircd::string_view
ircd::m::id::localname()
const
{
	auto ret{local()};
	assert(!ret.empty());
	ret.pop_front();
	return ret;
}

ircd::string_view
ircd::m::id::host()
const
{
	static const parser::rule<string_view> server_name
	{
		parser.server_name
	};

	static const parser::rule<string_view> rule
	{
		omit[parser.prefix >> ':'] >> raw[server_name]
	};

	string_view ret;
	auto *start{begin()};
	const auto res
	{
		qi::parse(start, end(), rule, ret)
	};

	assert(res || event::v4::is(*this) || event::v3::is(*this));
	assert(!res || !ret.empty());
	return ret;
}

ircd::string_view
ircd::m::id::local()
const
{
	static const parser::rule<string_view> prefix
	{
		parser.prefix
	};

	static const parser::rule<string_view> rule
	{
		eps > raw[prefix]
	};

	string_view ret;
	auto *start{begin()};
	qi::parse(start, end(), rule, ret);
	assert(!ret.empty());
	return ret;
}

//
// id::event
//

ircd::string_view
ircd::m::id::event::version()
const
{
	static const parser::rule<> is_v4
	{
		parser.event_id_v4 >> eoi
	};

	static const parser::rule<> is_v3
	{
		parser.event_id_v3 >> eoi
	};

	auto *start(std::begin(*this));
	auto *const stop(std::end(*this));

	return
		qi::parse(start, stop, is_v4)? "4":
		qi::parse(start, stop, is_v3)? "3":
		                               "1";
}

//
// id::event::v3
//

ircd::m::id::event::v3::v3(const string_view &id)
:id::event{id}
{
	if(unlikely(!is(id)))
		throw m::INVALID_MXID
		{
			"'%s' is not a version 3 event mxid; maybe version %s?",
			id,
			version(),
		};
}

ircd::m::id::event::v3::v3(const mutable_buffer &out,
                           const m::event &event)
:v3{[&out, event]
{
	if(unlikely(buffer::size(out) < 44))
		throw std::out_of_range
		{
			"Output buffer insufficient for v3 event_id"
		};

	thread_local char content_buffer[m::event::MAX_SIZE];
	const m::event essential
	{
		m::essential(event, content_buffer)
	};

	thread_local char preimage_buffer[m::event::MAX_SIZE];
	const json::object &preimage
	{
		json::stringify(preimage_buffer, essential)
	};

	const sha256::buf hash
	{
		sha256{preimage}
	};

	out[0] = '$';
	const string_view hashb64
	{
		b64::encode_unpadded(out + 1, hash)
	};

	return string_view
	{
		ircd::data(out), 1 + ircd::size(hashb64)
	};
}()}
{
}

bool
ircd::m::id::event::v3::is(const string_view &id)
noexcept
{
	static const parser::rule<> valid
	{
		parser.event_id_v3 >> eoi
	};

	auto *start(std::begin(id));
	auto *const stop(std::end(id));
	return qi::parse(start, stop, valid);
}

//
// id::event::v4
//

ircd::m::id::event::v4::v4(const string_view &id)
:id::event{id}
{
	if(unlikely(!is(id)))
		throw m::INVALID_MXID
		{
			"'%s' is not a version 4 event mxid; maybe version %s?",
			id,
			version(),
		};
}

ircd::m::id::event::v4::v4(const mutable_buffer &out,
                           const m::event &event)
:v4{[&out, event]
{
	if(unlikely(buffer::size(out) < 44))
		throw std::out_of_range
		{
			"Output buffer insufficient for v4 event_id"
		};

	thread_local char content_buffer[m::event::MAX_SIZE];
	const m::event essential
	{
		m::essential(event, content_buffer)
	};

	thread_local char preimage_buffer[m::event::MAX_SIZE];
	const json::object &preimage
	{
		json::stringify(preimage_buffer, essential)
	};

	const sha256::buf hash
	{
		sha256{preimage}
	};

	out[0] = '$';
	const string_view hashb64
	{
		b64::encode_unpadded<b64::urlsafe>(out + 1, hash)
	};

	return string_view
	{
		ircd::data(out), 1 + ircd::size(hashb64)
	};
}()}
{
}

bool
ircd::m::id::event::v4::is(const string_view &id)
noexcept
{
	static const parser::rule<> valid
	{
		parser.event_id_v4 >> eoi
	};

	auto *start(std::begin(id));
	auto *const stop(std::end(id));
	return qi::parse(start, stop, valid);
}

//
// util
//

bool
ircd::m::my(const id &id)
{
	assert(id.host());
	return my_host(id.host());
}

void
ircd::m::validate(const id::sigil &sigil,
                  const string_view &id)
{
	id::valid(sigil, id);
}

bool
ircd::m::valid(const id::sigil &sigil,
               const string_view &id)
noexcept
{
	return !empty(id) && id::valid(std::nothrow, sigil, id);
}

bool
ircd::m::valid_local_only(const id::sigil &sigil,
                          const string_view &id)
noexcept try
{
	static const id::parser::rule<> rule
	{
		id::parser.prefix
		| id::parser.event_id_v4
		| id::parser.event_id_v3
	};

	static const id::parser::rule<> test
	{
		rule >> eoi
	};

	const char *start(data(id)), *const &stop
	{
		start + std::min(size(id), id::MAX_SIZE)
	};

	return !empty(id) &&
	       id.at(0) == sigil &&
	       qi::parse(start, stop, test);
}
catch(...)
{
	return false;
}

bool
ircd::m::valid_local(const id::sigil &sigil,
                     const string_view &id)
noexcept try
{
	static const id::parser::rule<> test
	{
		id::parser.prefix
		| id::parser.event_id_v4
		| id::parser.event_id_v3
	};

	const char *start(data(id)), *const &stop
	{
		start + std::min(size(id), id::MAX_SIZE)
	};

	return !empty(id) &&
	       id.at(0) == sigil &&
	       qi::parse(start, stop, test);
}
catch(...)
{
	return false;
}

bool
ircd::m::has_sigil(const string_view &s)
noexcept try
{
	return is_sigil(s.at(0));
}
catch(...)
{
	return false;
}

bool
ircd::m::is_sigil(const char &c)
noexcept
{
	const char *start(&c), *const stop(start + 1);
	return qi::parse(start, stop, id::parser.sigil);
}

enum ircd::m::id::sigil
ircd::m::sigil(const string_view &s)
try
{
	return sigil(s.at(0));
}
catch(const std::out_of_range &e)
{
	throw BAD_SIGIL
	{
		"no sigil provided"
	};
}

enum ircd::m::id::sigil
ircd::m::sigil(const char &c)
{
	id::sigil ret;
	const char *start(&c), *const stop(&c + 1);
	if(!qi::parse(start, stop, id::parser.sigil, ret))
		throw BAD_SIGIL
		{
			"not a valid sigil"
		};

	assert(start == stop);
	return ret;
}

ircd::string_view
ircd::m::reflect(const id::sigil &c)
{
	switch(c)
	{
		case id::EVENT:        return "EVENT"_sv;
		case id::USER:         return "USER"_sv;
		case id::ROOM:         return "ROOM"_sv;
		case id::ROOM_ALIAS:   return "ROOM_ALIAS"_sv;
		case id::GROUP:        return "GROUP"_sv;
		case id::DEVICE:       return "DEVICE"_sv;
		case id::NODE:         return "NODE"_sv;
	}

	return "?????"_sv;
}

void
ircd::m::failure(const qi::expectation_failure<const char *> &e,
                 const string_view &goal)
{
	const auto rule
	{
		ircd::string(e.what_)
	};

	throw INVALID_MXID
	{
		"Not a valid %s because of an invalid %s.",
		goal,
		between(rule, '<', '>')
	};
}
