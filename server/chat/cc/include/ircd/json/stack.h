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
#define HAVE_IRCD_JSON_STACK_H

namespace ircd::json
{
	struct stack;
}

/// Output stack machine for stringifying JSON as-you-go. This device allows
/// the user to create JSON without knowing the contents when it is first
/// constructed. An object or array is opened and the user can append to the
/// stack creating the members or values or recursing further. The JSON is
/// then closed automatically with exception safety. Partial JSON is written
/// to the buffer as soon as possible.
///
/// The target buffer is not required to maintain earlier output from the same
/// stack or even earlier members and values of the same object or array. The
/// buffer may be smaller than the final JSON output and reused when the user
/// chooses to flush it to some storage or socket. If the buffer becomes full
/// a flush callback is attempted to make space and continue. This can occur
/// while the output is still incomplete JSON.
///
/// The user first creates a master json::stack instance with some reasonable
/// backing buffer. A suite of classes is provided to aid with building the
/// JSON which attach to each other stackfully, and eventually lead to the
/// root. There should only be one "active path" of instances at any given
/// time, ideally following the scope of your code itself. You must force
/// instances to go out of scope to continue at the same recursion depth.
/// This way the json::stack can "follow" your code and "record" the final
/// JSON output while allowing you to free the original resources required
/// for each value.
///
struct ircd::json::stack
{
	struct array;
	struct object;
	struct member;
	struct chase;
	struct const_chase;
	struct checkpoint;
	using flush_callback = std::function<const_buffer (const const_buffer &)>;

	window_buffer buf;
	flush_callback flusher;
	std::exception_ptr eptr;
	checkpoint *cp {nullptr};
	size_t appended {0};
	size_t flushed {0};
	size_t level {0};
	size_t hiwat;                      ///< autoflush watermark
	size_t lowat;                      ///< flush(false) call min watermark

	object *co {nullptr};              ///< The root object instance.
	array *ca {nullptr};               ///< Could be union with top_object but

	void rethrow_exception();
	void append(const size_t &expect, const window_buffer::closure &) noexcept;
	void append(const string_view &) noexcept;
	void append(const char &) noexcept;

  public:
	bool opened() const;               ///< Current stacking in progress.
	bool closed() const;               ///< No stacking in progress.
	bool failed() const;               ///< Exception pending in eptr.
	bool clean() const;                ///< Never opened.
	bool done() const;                 ///< Opened and closed.
	size_t remaining() const;
	string_view completed() const;

	size_t invalidate_checkpoints();
	bool flush(const bool &force = false) noexcept;
	size_t rewind(const size_t &bytes);
	void clear();

	stack(const mutable_buffer &,
	      flush_callback           = {},
	      const size_t &hiwat      = -1,
	      const size_t &lowat      = 0);

	stack(stack &&) noexcept;
	stack(const stack &) = delete;
	~stack() noexcept;
};

/// stack::member is an intermediary that is constructed under the scope of
/// a parent stack::object. It takes a name argument. It then requires one
/// object or array be constructed under its scope as its value, or a
/// json::value / already strung JSON must be appended as its value.
///
/// If the value is supplied in the constructor argument an instance of
/// this class does not have to be held (use constructor as function).
///
struct ircd::json::stack::member
{
	stack *s {nullptr};               ///< root stack ref
	object *po {nullptr};             ///< parent object
	string_view name;                 ///< member name state
	object *co {nullptr};             ///< current child object
	array *ca {nullptr};              ///< current child array
	bool vc {false};                  ///< value witnessed

	void _pre_append();
	void _post_append();

  public:
	template<class... T> void append(const json::tuple<T...> &);
	void append(const json::value &);

	member(object &po, const string_view &name);
	member(stack &s, const string_view &name);
	member(object &po, const string_view &name, const json::value &v);
	member(stack &s, const string_view &name, const json::value &);
	template<class... T> member(object &po, const string_view &name, const json::tuple<T...> &t);
	template<class... T> member(stack &s, const string_view &name, const json::tuple<T...> &t);
	member() = default;
	member(const member &) = delete;
	member(member &&) noexcept;
	~member() noexcept;

	static const member &top(const stack &);
	static member &top(stack &);
};

/// stack::object is constructed under the scope of either a stack::member,
/// or a stack::array, or a stack itself. Only stack::member can be
/// constructed directly under its scope.
///
/// For a stack::member parent, the named member is waiting for this value
/// after leaving the stack at ':' after the name, this object will then
/// print '{' and dtor with '}' and then return to the stack::member which
/// will then return to its parent object.
///
/// For a stack::array parent, the stack may have been left at '[' or ','
/// but either way this object will then print '{' and dtor with '}' and
/// then return to the stack::array.
///
/// For a stack itself, this object is considered the "top object" and will
/// open the stack with '{' and accept member instances under its scope
/// until closing the stack with '}' after which the stack is done()
///
struct ircd::json::stack::object
{
	member m;                          ///< optional internal member
	stack *s {nullptr};                ///< root stack ref
	member *pm {nullptr};              ///< parent member (if value of one)
	array *pa {nullptr};               ///< parent array (if value in one)
	member *cm {nullptr};              ///< current child member
	size_t mc {0};                     ///< members witnessed (monotonic)

  public:
	template<class... T> void append(const json::tuple<T...> &);
	void append(const json::object &);

	object(stack &s);                  ///< Object is top
	object(array &pa);                 ///< Object is value in the array
	object(member &pm);                ///< Object is value of named member
	object(object &po, const string_view &name);
	object(stack &s, const string_view &name);
	object(object &&) noexcept;
	object(const object &) = delete;
	~object() noexcept;

	static const object &top(const stack &);
	static object &top(stack &);
};

/// stack::array is constructed under the scope of either a stack::member,
/// or a stack::array, or a stack itself. stack::object and stack::array
/// can be constructed directly under its scope, but not stack::member.
///
/// The same behavior as described by stack::object documentation applies
/// here translated to arrays.
///
struct ircd::json::stack::array
{
	member m;                          ///< optional internal member
	stack *s {nullptr};                ///< root stack ref
	member *pm {nullptr};              ///< parent member (if value of one)
	array *pa {nullptr};               ///< parent array (if value in one)
	object *co {nullptr};              ///< current child object
	array *ca {nullptr};               ///< current child array
	size_t vc {0};                     ///< values witnessed (monotonic)

	void _pre_append();
	void _post_append();

  public:
	template<class... T> void append(const json::tuple<T...> &);
	void append(const json::value &);

	array(member &pm);                 ///< Array is value of the named member
	array(array &pa);                  ///< Array is value in the array
	array(object &po, const string_view &name);
	array(stack &s, const string_view &name);
	array(stack &s);
	array(const array &) = delete;
	array(array &&) noexcept;
	~array() noexcept;

	static const array &top(const stack &);
	static array &top(stack &);
};

/// This device chases the current active path by updating its member pointers.
struct ircd::json::stack::chase
{
	array *a {nullptr};
	object *o {nullptr};
	member *m {nullptr};

	bool next();
	bool prev();

	chase(stack &s, const bool &prechase = false);
	chase() = default;
};

/// This device chases the current active path by updating its member pointers.
struct ircd::json::stack::const_chase
{
	const array *a {nullptr};
	const object *o {nullptr};
	const member *m {nullptr};

	bool next();
	bool prev();

	const_chase(const stack &s, const bool &prechase = false);
	const_chase() = default;
};

/// Checkpoint captures the current state of the json::stack on construction
/// and allows a restoration to that state in one of three ways:
///
/// - Calling rollback() will immediately rewind the json::stack buffer and
/// allow continuing from the check point. This should be used with care, as
/// other json::stack objects may still be pending on the stack and destruct
/// after calling rollback(), leaving an incoherent attempt to close the JSON.
///
/// - Calling decommit() will defer the rollback() until destruction time. Take
/// care again that the checkpoint was still placed on the stack to avoid the
/// rollback() pitfall.
///
/// - Destruction under an exception is equivalent to a decommit() and will
/// perform a rollback() if exception_rollback is set.
///
/// Flushes are avoided under the scope of a checkpoint, but they are still
/// forced if the json::stack buffer fills up. In this case all active
/// checkpoints are invalidated and cannot be rolled back.
///
struct ircd::json::stack::checkpoint
{
	stack *s {nullptr};
	checkpoint *pc {nullptr};
	size_t point {0};
	size_t vc {0};
	bool committed {true};
	bool exception_rollback {true};

  public:
	bool committing() const noexcept;         ///< When false, destructor will rollback()
	bool committing(const bool &) noexcept;   ///< Sets committing() to value.
	bool rollback();                          ///< Performs rollback of buffer.

	checkpoint(stack &s,
	           const bool &committed = true,
	           const bool &exception_rollback = true);

	checkpoint(checkpoint &&) = delete;
	checkpoint(const checkpoint &) = delete;
	~checkpoint() noexcept;
};

template<class... T>
ircd::json::stack::member::member(stack &s,
                                  const string_view &name,
                                  const json::tuple<T...> &t)
:member
{
	object::top(s), name, t
}
{}

template<class... T>
ircd::json::stack::member::member(object &po,
                                  const string_view &name,
                                  const json::tuple<T...> &t)
:member{po, name}
{
	append(t);
}

template<class... T>
void
ircd::json::stack::member::append(const json::tuple<T...> &t)
{
	_pre_append();
	const unwind post{[this]
	{
		_post_append();
	}};

	s->append(serialized(t), [&t](mutable_buffer buf)
	{
		return ircd::size(stringify(buf, t));
	});
}

template<class... T>
void
ircd::json::stack::array::append(const json::tuple<T...> &t)
{
	_pre_append();
	const unwind post{[this]
	{
		_post_append();
	}};

	s->append(serialized(t), [&t](mutable_buffer buf)
	{
		return ircd::size(stringify(buf, t));
	});
}

template<class... T>
void
ircd::json::stack::object::append(const json::tuple<T...> &t)
{
	for_each(t, [this](const auto &name, const auto &_value)
	{
		const json::value value
		{
			_value
		};

		if(defined(value))
			json::stack::member
			{
				*this, name, value
			};
	});
}

inline bool
ircd::json::stack::checkpoint::committing(const bool &committed)
noexcept
{
	const bool ret(this->committed);
	this->committed = committed;
	return ret;
}

inline bool
ircd::json::stack::checkpoint::committing()
const noexcept
{
	return committed;
}
