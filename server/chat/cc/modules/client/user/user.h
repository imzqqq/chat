// Matrix Construct
//
// Copyright (C) Matrix Construct Developers, Authors & Contributors
// Copyright (C) 2016-2018 Jason Volk <jason@zemos.net>
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice is present in all copies. The
// full license for this software is available in the LICENSE file.

extern ircd::m::resource user_resource;

///////////////////////////////////////////////////////////////////////////////
//
// filter.cc
//

ircd::m::resource::response
get__filter(ircd::client &,
            const ircd::m::resource::request &,
            const ircd::m::user::id &);

ircd::m::resource::response
post__filter(ircd::client &,
             const ircd::m::resource::request::object<const ircd::m::filter> &,
             const ircd::m::user::id &);

///////////////////////////////////////////////////////////////////////////////
//
// account_data.cc
//

ircd::m::resource::response
put__account_data(ircd::client &client,
                  const ircd::m::resource::request &request,
                  const ircd::m::user &user);

ircd::m::resource::response
get__account_data(ircd::client &client,
                  const ircd::m::resource::request &request,
                  const ircd::m::user &user);

///////////////////////////////////////////////////////////////////////////////
//
// openid.cc
//

ircd::m::resource::response
post__openid(ircd::client &client,
             const ircd::m::resource::request &request,
             const ircd::m::user::id &user_id);

///////////////////////////////////////////////////////////////////////////////
//
// rooms.cc
//

ircd::m::resource::response
get__rooms(ircd::client &client,
           const ircd::m::resource::request &request,
           const ircd::m::user::id &user_id);

ircd::m::resource::response
put__rooms(ircd::client &client,
           const ircd::m::resource::request &request,
           const ircd::m::user::id &user_id);

ircd::m::resource::response
delete__rooms(ircd::client &client,
              const ircd::m::resource::request &request,
              const ircd::m::user::id &user_id);
