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
#define HAVE_IRCD_DB_OPTS_H

namespace ircd::db
{
	enum class set :uint64_t;
	enum class get :uint64_t;

	struct options;
	template<class> struct opts;
	struct sopts;
	struct gopts;

	template<class T> bool test(const opts<T> &, const typename std::underlying_type<T>::type &);
	template<class T> bool test(const opts<T> &, const T &value);

	template<class T> opts<T> &operator|=(opts<T> &, const opts<T> &);
	template<class T> opts<T> &operator|=(opts<T> &, const T &value);

	template<class T> opts<T> &operator&=(opts<T> &, const opts<T> &);
	template<class T> opts<T> &operator&=(opts<T> &, const T &value);
}

enum class ircd::db::set
:uint64_t
{
	FSYNC            = 0x0001, ///< Uses kernel filesystem synchronization after write (slow).
	NO_JOURNAL       = 0x0002, ///< Write Ahead Log (WAL) for some crash recovery (danger).
	NO_BLOCKING      = 0x0004, ///< Fail if write would block.
	NO_COLUMN_ERR    = 0x0008, ///< No exception thrown when writing to a deleted column.
	PRIO_LOW         = 0x0010, ///< Mark for low priority behavior.
	PRIO_HIGH        = 0x0020, ///< Mark for high priority behavior.
};

enum class ircd::db::get
:uint64_t
{
	PIN              = 0x0001, ///< Keep iter data in memory for iter lifetime (good for lots ++/--).
	PREFIX           = 0x0002, ///< (prefix_same_as_start); automatic for index columns with pfx.
	ORDERED          = 0x0004, ///< (total_order_seek); relevant to index columns.
	CACHE            = 0x0008, ///< Update the cache.
	NO_CACHE         = 0x0010, ///< Do not update the cache.
	CHECKSUM         = 0x0020, ///< Integrity of data will be checked (overrides conf).
	NO_CHECKSUM      = 0x0040, ///< Integrity of data will not be checked (overrides conf).
	NO_BLOCKING      = 0x0080, ///< Fail if read would block from not being cached.
	NO_SNAPSHOT      = 0x0100, ///< This iterator will have the latest data (tailing).
	NO_PARALLEL      = 0x0200, ///< Don't submit requests in parallel (relevant to db::row).
	THROW            = 0x0400, ///< Throw exceptions more than usual.
	NO_THROW         = 0x0800, ///< Suppress exceptions if possible.
};

template<class T>
struct ircd::db::opts
{
	using flag_t = typename std::underlying_type<T>::type;

	flag_t value {0};

	opts() = default;
	opts(const std::initializer_list<T> &list)
	:value{combine_flags(list)}
	{}
};

/// options for writing (set)
struct ircd::db::sopts
:opts<set>
{
	using opts<set>::opts;
};

/// options for reading (get)
struct ircd::db::gopts
:opts<get>
{
	database::snapshot snapshot;
	const rocksdb::Slice *lower_bound { nullptr };
	const rocksdb::Slice *upper_bound { nullptr };
	size_t readahead { 0 };
	uint64_t seqnum { 0 };

	using opts<get>::opts;
};

/// options <-> string
struct ircd::db::options
:std::string
{
	struct map;

	// Output of options structures from this string
	explicit operator rocksdb::Options() const;
	operator rocksdb::DBOptions() const;
	operator rocksdb::ColumnFamilyOptions() const;
	operator rocksdb::PlainTableOptions() const;
	operator rocksdb::BlockBasedTableOptions() const;

	// Input of options structures output to this string
	explicit options(const rocksdb::ColumnFamilyOptions &);
	explicit options(const rocksdb::DBOptions &);
	explicit options(const database::column &);
	explicit options(const database &);

	// Input of options string from user
	options(std::string string)
	:std::string{std::move(string)}
	{}
};

/// options <-> map
struct ircd::db::options::map
:std::unordered_map<std::string, std::string>
{
	// Output of options structures from map
	operator rocksdb::DBOptions() const;
	operator rocksdb::ColumnFamilyOptions() const;
	operator rocksdb::PlainTableOptions() const;
	operator rocksdb::BlockBasedTableOptions() const;

	// Convert option string to map
	map(const options &);

	// Input of options map from user
	map(std::unordered_map<std::string, std::string> m)
	:std::unordered_map<std::string, std::string>{std::move(m)}
	{}
};

template<class T>
inline ircd::db::opts<T> &
ircd::db::operator&=(opts<T> &a,
                     const T &value)
{
	using flag_t = typename opts<T>::flag_t;

	a.value &= flag_t(value);
	return a;
}

template<class T>
inline ircd::db::opts<T> &
ircd::db::operator&=(opts<T> &a,
                     const opts<T> &b)
{
	a.value &= b.value;
	return a;
}

template<class T>
inline ircd::db::opts<T> &
ircd::db::operator|=(opts<T> &a,
                     const T &value)
{
	using flag_t = typename opts<T>::flag_t;

	a.value |= flag_t(value);
	return a;
}

template<class T>
inline ircd::db::opts<T> &
ircd::db::operator|=(opts<T> &a,
                     const opts<T> &b)
{
	a.value |= b.value;
	return a;
}

template<class T>
inline bool
ircd::db::test(const opts<T> &a,
               const T &value)
{
	using flag_t = typename opts<T>::flag_t;

	return a.value & flag_t(value);
}

template<class T>
inline bool
ircd::db::test(const opts<T> &a,
               const typename std::underlying_type<T>::type &value)
{
	using flag_t = typename opts<T>::flag_t;

	return a.value & flag_t(value);
}
