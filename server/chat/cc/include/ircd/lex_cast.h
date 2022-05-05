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
#define HAVE_IRCD_LEX_CAST_H

//
// Lexical conversions
//
namespace ircd
{
	IRCD_EXCEPTION_HIDENAME(ircd::error, bad_lex_cast)

	// Test if lex_cast will succeed
	template<class T> bool lex_castable(const string_view &) noexcept = delete;

	// Convert from ascii to integral
	template<class T> T lex_cast(std::string &);
	template<class T> T lex_cast(const std::string &);
	template<class T> T lex_cast(const std::string_view &);
	template<class T> T lex_cast(const string_view &);

	// Convert from integral to ascii (user supplied destination buffer)
	template<class T> string_view lex_cast(T, const mutable_buffer &buf);

	// Convert from integral to ascii (circular internal thread_local buffer)
	template<class T> string_view lex_cast(const T &t);
}

// Internal convenience ring buffer related
namespace ircd
{
	constexpr size_t LEX_CAST_BUFS {256}, LEX_CAST_BUFSIZE {64};
	extern thread_local char lex_cast_buf[LEX_CAST_BUFS][LEX_CAST_BUFSIZE];
	extern thread_local uint8_t lex_cast_buf_head;
}

// Explicit linkages
namespace ircd
{
	template<> bool lex_castable<std::string>(const string_view &) noexcept;       // stub always true
	template<> bool lex_castable<std::string_view>(const string_view &) noexcept;  // stub always true
	template<> bool lex_castable<string_view>(const string_view &) noexcept;       // stub always true
	template<> bool lex_castable<long double>(const string_view &) noexcept;
	template<> bool lex_castable<double>(const string_view &) noexcept;
	template<> bool lex_castable<float>(const string_view &) noexcept;
	template<> bool lex_castable<ulong>(const string_view &) noexcept;
	template<> bool lex_castable<long>(const string_view &) noexcept;
	template<> bool lex_castable<uint>(const string_view &) noexcept;
	template<> bool lex_castable<int>(const string_view &) noexcept;
	template<> bool lex_castable<ushort>(const string_view &) noexcept;
	template<> bool lex_castable<short>(const string_view &) noexcept;
	template<> bool lex_castable<uint8_t>(const string_view &) noexcept;
	template<> bool lex_castable<int8_t>(const string_view &) noexcept;
	template<> bool lex_castable<bool>(const string_view &) noexcept;
	template<> bool lex_castable<seconds>(const string_view &) noexcept;
	template<> bool lex_castable<milliseconds>(const string_view &) noexcept;
	template<> bool lex_castable<microseconds>(const string_view &) noexcept;
	template<> bool lex_castable<nanoseconds>(const string_view &) noexcept;

	template<> std::string &lex_cast(std::string &);                          // trivial
	template<> std::string lex_cast(const std::string &);                     // trivial
	template<> std::string_view lex_cast(const std::string_view &);           // trivial
	template<> std::string lex_cast(const string_view &);                     // trivial
	template<> long double lex_cast(const string_view &);
	template<> double lex_cast(const string_view &);
	template<> float lex_cast(const string_view &);
	template<> ulong lex_cast(const string_view &);
	template<> long lex_cast(const string_view &);
	template<> uint lex_cast(const string_view &);
	template<> int lex_cast(const string_view &);
	template<> ushort lex_cast(const string_view &);
	template<> short lex_cast(const string_view &);
	template<> uint8_t lex_cast(const string_view &);
	template<> int8_t lex_cast(const string_view &);
	template<> bool lex_cast(const string_view &);
	template<> seconds lex_cast(const string_view &);
	template<> milliseconds lex_cast(const string_view &);
	template<> microseconds lex_cast(const string_view &);
	template<> nanoseconds lex_cast(const string_view &);

	template<> string_view lex_cast(const std::string &, const mutable_buffer &buf);
	template<> string_view lex_cast(const std::string_view &, const mutable_buffer &buf);
	template<> string_view lex_cast(const string_view &, const mutable_buffer &buf);
	template<> string_view lex_cast(seconds, const mutable_buffer &buf);
	template<> string_view lex_cast(milliseconds, const mutable_buffer &buf);
	template<> string_view lex_cast(microseconds, const mutable_buffer &buf);
	template<> string_view lex_cast(nanoseconds, const mutable_buffer &buf);
	template<> string_view lex_cast(long double, const mutable_buffer &buf);
	template<> string_view lex_cast(double, const mutable_buffer &buf);
	template<> string_view lex_cast(float, const mutable_buffer &buf);
	template<> string_view lex_cast(ulong, const mutable_buffer &buf);
	template<> string_view lex_cast(long, const mutable_buffer &buf);
	template<> string_view lex_cast(uint, const mutable_buffer &buf);
	template<> string_view lex_cast(int, const mutable_buffer &buf);
	template<> string_view lex_cast(ushort, const mutable_buffer &buf);
	template<> string_view lex_cast(short, const mutable_buffer &buf);
	template<> string_view lex_cast(uint8_t, const mutable_buffer &buf);
	template<> string_view lex_cast(int8_t, const mutable_buffer &buf);
	template<> string_view lex_cast(bool, const mutable_buffer &buf);
}

#ifdef __clang__
	#define IRCD_LEX_CAST_UNNECESSARY \
		__attribute__((deprecated("unnecessary lexical cast")))
#else
	#define IRCD_LEX_CAST_UNNECESSARY \
		__attribute__((warning("unnecessary lexical cast")))
#endif

/// Convert a native number to a string. The returned value is a view of the
/// string in a static ring buffer. There are LEX_CAST_BUFS number of buffers
/// so you should not hold on to the returned view for very long.
template<class T>
inline ircd::string_view
ircd::lex_cast(const T &t)
{
	return lex_cast<T>(t, lex_cast_buf[lex_cast_buf_head++]);
}

/// Conversion to an std::string creates a copy when the input is a
/// string_view. Note this is not considered an "unnecessary lexical cast"
/// even though nothing is being converted, so there will be no warning.
template<>
inline std::string
ircd::lex_cast<std::string>(const string_view &s)
{
	return std::string{s};
}

/// Template basis for a string_view input
template<class T>
inline T
ircd::lex_cast(const string_view &s)
{
	return s;
}

/// Specialization of a string_view to string_view conversion which is just
/// a trivial copy of the view.
template<>
inline std::string_view
ircd::lex_cast<std::string_view>(const std::string_view &s)
{
	return s;
}

/// Specialization of a string to string conversion which generates a warning
/// because the conversion has to copy the string while no numerical conversion
/// has taken place. The developer should remove the offending lex_cast.
template<>
IRCD_LEX_CAST_UNNECESSARY
inline std::string
ircd::lex_cast<std::string>(const std::string &s)
{
	return s;
}

/// Template basis for a const std::string input
template<class T>
inline T
ircd::lex_cast(const std::string &s)
{
	return lex_cast<T>(string_view{s});
}

/// Template basis for an lvalue string. If we can get this binding rather
/// than the const std::string alternative some trivial conversions are
/// easier to make in the specializations.
template<class T>
inline T
ircd::lex_cast(std::string &s)
{
	return lex_cast<T>(string_view{s});
}

/// Specialization of a string to string conversion without a warning because
/// we can trivially pass through a reference from input to output.
template<>
inline std::string &
ircd::lex_cast(std::string &s)
{
	return s;
}

/// Specialization of a string to string conversion to user's buffer;
/// marked as unnecessary because no numerical conversion takes place yet
/// data is still copied. (note: warning may be removed; may be intentional)
template<>
IRCD_LEX_CAST_UNNECESSARY
inline ircd::string_view
ircd::lex_cast(const string_view &s,
               const mutable_buffer &buf)
{
	s.copy(data(buf), size(buf));
	return { data(buf), s.size() };
}

/// Specialization of a string to string conversion to user's buffer;
/// marked as unnecessary because no numerical conversion takes place yet
/// data is still copied. (note: warning may be removed; may be intentional)
template<>
IRCD_LEX_CAST_UNNECESSARY
inline ircd::string_view
ircd::lex_cast(const std::string_view &s,
               const mutable_buffer &buf)
{
	s.copy(data(buf), size(buf));
	return { data(buf), s.size() };
}

/// Specialization of a string to string conversion to user's buffer;
/// marked as unnecessary because no numerical conversion takes place yet
/// data is still copied. (note: warning may be removed; may be intentional)
template<>
IRCD_LEX_CAST_UNNECESSARY
inline ircd::string_view
ircd::lex_cast(const std::string &s,
               const mutable_buffer &buf)
{
	s.copy(data(buf), size(buf));
	return { data(buf), s.size() };
}

/// Template basis; if no specialization is matched there is no fallback here
template<class T>
IRCD_LEX_CAST_UNNECESSARY
inline ircd::string_view
ircd::lex_cast(T t,
               const mutable_buffer &buf)
{
	assert(0);
	return {};
}

/// Trivial conversion; always returns true
template<>
inline bool
ircd::lex_castable<ircd::string_view>(const string_view &)
noexcept
{
	return true;
}

/// Trivial conversion; always returns true
template<>
inline bool
ircd::lex_castable<std::string_view>(const string_view &)
noexcept
{
	return true;
}

/// Trivial conversion; always returns true
template<>
inline bool
ircd::lex_castable<std::string>(const string_view &s)
noexcept
{
	return true;
}
