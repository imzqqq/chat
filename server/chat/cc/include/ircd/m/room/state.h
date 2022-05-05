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
#define HAVE_IRCD_M_ROOM_STATE_H

/// Interface to room state.
///
/// This interface focuses specifically on the details of room state. Most of
/// the queries to this interface respond in logarithmic time. If an event with
/// a state_key is present in room::events but it is not present in room::state
/// or room::state::space it was accepted into the room but we will not apply
/// it to our machine, though other parties may (this is a state-conflict).
///
struct ircd::m::room::state
{
	struct opts;
	struct space;
	struct history;
	struct rebuild;
	struct fetch;

	using closure = std::function<void (const string_view &, const string_view &, const event::idx &)>;
	using closure_bool = std::function<bool (const string_view &, const string_view &, const event::idx &)>;
	IRCD_STRONG_TYPEDEF(string_view, type_prefix)

	static conf::item<bool> enable_history;
	static conf::item<size_t> readahead_size;

	room::id room_id;
	event::id::buf event_id;
	const event::fetch::opts *fopts {nullptr};
	mutable bool _not_present {false}; // cached result of !present()

	// Check if this object is representing the present state or a past state.
	bool present() const;

	// Iterate the state
	bool for_each(const string_view &type, const string_view &lower_bound, const closure_bool &view) const;
	bool for_each(const string_view &type, const string_view &lower_bound, const event::closure_idx_bool &view) const;
	bool for_each(const string_view &type, const string_view &lower_bound, const event::id::closure_bool &view) const;
	bool for_each(const string_view &type, const string_view &lower_bound, const event::closure_bool &view) const;
	bool for_each(const string_view &type, const closure_bool &view) const;
	bool for_each(const string_view &type, const event::closure_idx_bool &view) const;
	void for_each(const string_view &type, const event::closure_idx &) const;
	bool for_each(const string_view &type, const event::id::closure_bool &view) const;
	void for_each(const string_view &type, const event::id::closure &) const;
	bool for_each(const string_view &type, const event::closure_bool &view) const;
	void for_each(const string_view &type, const event::closure &) const;
	bool for_each(const type_prefix &type, const closure_bool &view) const;
	bool for_each(const closure_bool &view) const;
	bool for_each(const event::closure_idx_bool &view) const;
	void for_each(const event::closure_idx &) const;
	bool for_each(const event::id::closure_bool &view) const;
	void for_each(const event::id::closure &) const;
	bool for_each(const event::closure_bool &view) const;
	void for_each(const event::closure &) const;

	// Counting / Statistics
	size_t count(const string_view &type) const;
	size_t count() const;

	// Existential
	bool has(const string_view &type, const string_view &state_key) const;
	bool has(const string_view &type) const;
	bool has(const event::idx &) const;

	// Fetch a state event
	bool get(std::nothrow_t, const string_view &type, const string_view &state_key, const event::closure_idx &) const;
	bool get(std::nothrow_t, const string_view &type, const string_view &state_key, const event::id::closure &) const;
	bool get(std::nothrow_t, const string_view &type, const string_view &state_key, const event::closure &) const;
	void get(const string_view &type, const string_view &state_key, const event::closure_idx &) const;
	void get(const string_view &type, const string_view &state_key, const event::id::closure &) const;
	void get(const string_view &type, const string_view &state_key, const event::closure &) const;
	event::idx get(std::nothrow_t, const string_view &type, const string_view &state_key = "") const;
	event::idx get(const string_view &type, const string_view &state_key = "") const;

	// Prefetch state cells
	bool prefetch(const string_view &type, const string_view &state_key) const;
	bool prefetch(const string_view &type) const;

	state(const m::room &room, const event::fetch::opts *const & = nullptr);
	state() = default;
	state(const state &) = delete;
	state &operator=(const state &) = delete;

	static bool prev(const event::idx &, const event::closure_idx_bool &);
	static bool next(const event::idx &, const event::closure_idx_bool &);
	static event::idx prev(const event::idx &);
	static event::idx next(const event::idx &);

	static bool present(const event::idx &);
	static size_t purge_replaced(const room::id &);
	static bool is(std::nothrow_t, const event::idx &);
	static bool is(const event::idx &);
};

struct ircd::m::room::state::rebuild
{
	rebuild(const room::id &);
};
