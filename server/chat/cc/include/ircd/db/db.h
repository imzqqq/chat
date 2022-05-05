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
#define HAVE_IRCD_DB_H

/// Database: an object store from the primitives of `cell`, `column`, and `row`.
namespace ircd::db
{
	struct init;
	struct error;
	struct gopts;
	struct sopts;
	struct cell;
	struct row;
	struct column;
	struct index;
	struct database;
	struct options;

	// db subsystem has its own logging facility
	extern struct log::log log;

	// Version information from rocksdb headers and library
	extern const info::versions version_api, version_abi;

	// Supported compressions (detected when running ircd)
	extern std::array<std::tuple<std::string, ulong>, 16> compressions;
}

#include "pos.h"
#include "delta.h"
#include "comparator.h"
#include "compactor.h"
#include "prefix_transform.h"
#include "merge.h"
#include "descriptor.h"
#include "rocksdb.h"
#include "database.h"
#include "snapshot.h"
#include "sst.h"
#include "wal.h"
#include "error.h"
#include "cache.h"
#include "opts.h"
#include "column.h"
#include "column_iterator.h"
#include "domain.h"
#include "cell.h"
#include "row.h"
#include "json.h"
#include "txn.h"
#include "prefetcher.h"
#include "stats.h"

//
// Misc utils
//
namespace ircd::db
{
	// Utils for "name:checkpoint" string amalgam
	std::string namepoint(const string_view &name, const uint64_t &checkpoint);
	std::pair<string_view, uint64_t> namepoint(const string_view &name);

	// Generate local filesytem path based on name / name:checkpoint / etc.
	std::string path(const string_view &name, const uint64_t &checkpoint);
	std::string path(const string_view &name);

	// Paths of available databases.
	std::vector<std::string> available();
}

namespace ircd
{
	using db::database;
}

/// Database subsystem initialization and destruction
struct ircd::db::init
{
	static const std::string direct_io_test_file_path;

	static void request_pool();
	static void compressions();
	static void directory();
	static void test_direct_io();
	static void test_hw_crc32();

  public:
	init();
	~init() noexcept;
};
