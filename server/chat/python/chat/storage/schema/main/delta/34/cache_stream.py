import logging

from chat.storage.engines import PostgresEngine
from chat.storage.prepare_database import get_statements

logger = logging.getLogger(__name__)


# This stream is used to notify replication slaves that some caches have
# been invalidated that they cannot infer from the other streams.
CREATE_TABLE = """
CREATE TABLE cache_invalidation_stream (
    stream_id       BIGINT,
    cache_func      TEXT,
    keys            TEXT[],
    invalidation_ts BIGINT
);

CREATE INDEX cache_invalidation_stream_id ON cache_invalidation_stream(stream_id);
"""


def run_create(cur, database_engine, *args, **kwargs):
    if not isinstance(database_engine, PostgresEngine):
        return

    for statement in get_statements(CREATE_TABLE.splitlines()):
        cur.execute(statement)


def run_upgrade(cur, database_engine, *args, **kwargs):
    pass
