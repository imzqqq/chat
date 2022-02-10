// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

#pragma once
#define HAVE_IRCD_JSON_H

/// JavaScript Object Notation: formal grammars & tools
///
namespace ircd::json
{
	IRCD_EXCEPTION(ircd::error, error);
	IRCD_PANICKING(error, print_panic);
	IRCD_EXCEPTION(error, print_error);
	IRCD_EXCEPTION(error, parse_error);
	IRCD_EXCEPTION(error, type_error);
	IRCD_EXCEPTION(error, not_found);
	IRCD_EXCEPTION(parse_error, recursion_limit);

	template<class... T>
	string_view stringify(const mutable_buffer &&mb, T&&... t);
}

#include "type.h"
#include "util.h"
#include "string.h"
#include "array.h"
#include "object.h"
#include "vector.h"
#include "value.h"
#include "member.h"
#include "iov.h"
#include "strung.h"
#include "tuple/tuple.h"
#include "stack.h"

// Convenience toolset for higher level operations.
namespace ircd::json
{
	void merge(stack::object &out, const vector &);
	strung remove(const object &, const string_view &key);
	strung remove(const object &, const size_t &index);
	strung insert(const object &, const member &);
	strung replace(const object &, const member &);
	strung replace(const object &,  const members &);
}

// Exports to ircd::
namespace ircd
{
	using json::operator ""_;
	using json::operator<<;
	using json::defined;
	using json::for_each;
	using json::until;
	using json::get;
}

/// Convenience template for const rvalue mutable_buffers or basically
/// allowing a bracket initialization of a mutable_buffer in the argument
/// to stringify(). The const rvalue reference is on purpose. The stringify()
/// family of friends all use a non-const lvalue mutable_buffer as an append
/// only "stream" buffer. We don't want to modify any non-const instances of
/// the mutable_buffer you pass here by offering a `mutable_buffer &` overload
///
template<class... T>
ircd::string_view
ircd::json::stringify(const mutable_buffer &&mb_,
                      T&&... t)
{
	mutable_buffer mb(mb_);
	const auto ret(stringify(mb, std::forward<T>(t)...));
	debug_valid_output(ret, serialized(std::forward<T>(t)...));
	return ret;
}
