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
#define HAVE_IRCD_M_EDU_H

namespace ircd::m
{
	struct edu;
}

struct ircd::m::edu
:json::tuple
<
	json::property<name::edu_type, json::string>,
	json::property<name::content, json::object>
>
{
	struct m_presence;
	struct m_typing;
	struct m_receipt;
	struct m_direct_to_device;

	using super_type::tuple;
	using super_type::operator=;
};
