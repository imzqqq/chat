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
#define HAVE_IRCD_JSON_ARRAY_H

namespace ircd::json
{
	struct array;

	bool empty(const array &);
	bool operator!(const array &);
	size_t size(const array &);

	size_t serialized(const string_view *const &begin, const string_view *const &end);
	size_t serialized(const std::string *const &begin, const std::string *const &end);
	size_t serialized(const array &);

	string_view stringify(mutable_buffer &buf, const string_view *const &begin, const string_view *const &end);
	string_view stringify(mutable_buffer &buf, const std::string *const &begin, const std::string *const &end);
	string_view stringify(mutable_buffer &, const array &);
	std::ostream &operator<<(std::ostream &, const array &);
}

/// Lightweight interface to a JSON array string.
///
/// This object accepts queries with numerical indexing. The same parsing
/// approach is used in ircd::json::object and that is important to note here:
/// iterating this array by incrementing your own numerical index and making
/// calls into this object is NOT efficient. Simply put, do not do something
/// like `for(int x=0; x<array.count(); x++) array.at(x)` as that will parse
/// the array from the beginning on every single increment. Instead, use the
/// provided iterator object.
///
struct ircd::json::array
:string_view
{
	struct const_iterator;

	using value_type = const string_view;
	using pointer = value_type *;
	using reference = value_type &;
	using iterator = const_iterator;
	using size_type = size_t;
	using difference_type = ptrdiff_t;

	static const uint max_recursion_depth;

	const_iterator end() const;
	const_iterator begin() const;
	const_iterator find(size_t i) const;

	bool empty() const;
	size_t count() const;
	size_t size() const;

	template<class T> T at(const size_t &i) const;
	string_view at(const size_t &i) const;
	string_view operator[](const size_t &i) const;

	explicit operator std::string() const;

	using string_view::string_view;

	template<class it> static size_t serialized(const it &b, const it &e);
	template<class it> static string_view stringify(mutable_buffer &, const it &b, const it &e);
};

#include "array_iterator.h"

template<class T>
inline T
ircd::json::array::at(const size_t &i)
const try
{
	return lex_cast<T>(at(i));
}
catch(const bad_lex_cast &e)
{
	throw type_error
	{
		"indice %zu must cast to type %s", i, typeid(T).name()
	};
}

inline ircd::json::array::const_iterator
ircd::json::array::end()
const
{
	return { string_view::end(), string_view::end() };
}

inline bool
ircd::json::array::empty()
const
{
	const string_view &sv{*this};
	// Allow empty objects '{}' to pass this assertion; this function is not
	// a type-check. Some serializers (like browser JS) might give an empty
	// object before it has any context that the set is an array; it doesn't
	// matter here for us.
	assert(sv.size() > 2 || sv.empty() || sv == empty_array || sv == empty_object);
	return sv.size() <= 2;
}
