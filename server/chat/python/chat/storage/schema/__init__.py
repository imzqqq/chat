SCHEMA_VERSION = 63
"""Represents the expectations made by the codebase about the database schema

This should be incremented whenever the codebase changes its requirements on the
shape of the database schema (even if those requirements are backwards-compatible with
older versions of Chat server).

See https://chat.docs.imzqqq.top/development/database_schema.html
for more information on how this works.

Changes in SCHEMA_VERSION = 61:
    - The `user_stats_historical` and `room_stats_historical` tables are not written and
      are not read (previously, they were written but not read).

Changes in SCHEMA_VERSION = 63:
    - The `public_room_list_stream` table is not written nor read to
      (previously, it was written and read to, but not for any significant purpose).
      https://github.com/matrix-org/chat/pull/10565
"""


SCHEMA_COMPAT_VERSION = 59
"""Limit on how far the Chat server codebase can be rolled back without breaking db compat

This value is stored in the database, and checked on startup. If the value in the
database is greater than SCHEMA_VERSION, then Chat server will refuse to start.
"""
