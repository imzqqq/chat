"""
Adds a postgres SEQUENCE for generating guest user IDs.
"""

from chat.storage.databases.main.registration import (
    find_max_generated_user_id_localpart,
)
from chat.storage.engines import PostgresEngine


def run_create(cur, database_engine, *args, **kwargs):
    if not isinstance(database_engine, PostgresEngine):
        return

    next_id = find_max_generated_user_id_localpart(cur) + 1
    cur.execute("CREATE SEQUENCE user_id_seq START WITH %s", (next_id,))


def run_upgrade(*args, **kwargs):
    pass
