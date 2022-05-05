// The Construct
//
// Copyright (C) The Construct Developers, Authors & Contributors
// Copyright (C) 2016-2020 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

#pragma once
#define HAVE_IRCD_PROF_SYSCALL_USAGE_WARNING_H

namespace ircd::prof
{
	struct syscall_usage_warning;
}

#ifdef RB_DEBUG
struct ircd::prof::syscall_usage_warning
{
	const string_view fmt;
	const va_rtti ap;
	ircd::prof::syscall_timer timer;

	template<class... args>
	syscall_usage_warning(const string_view &fmt, args&&... a)
	:syscall_usage_warning{fmt, va_rtti{std::forward<args>(a)...}}
	{}

	explicit syscall_usage_warning(const string_view &fmt, va_rtti &&ap)
	:fmt{fmt}
	,ap{std::move(ap)}
	{}

	~syscall_usage_warning() noexcept;
};
#else
struct ircd::prof::syscall_usage_warning
{
	syscall_usage_warning(const string_view &fmt, ...)
	{
		// In at least gcc 6.3.0 20170519, template param packs are not DCE'ed
		// so legacy varargs are used here.
	}
};
#endif
