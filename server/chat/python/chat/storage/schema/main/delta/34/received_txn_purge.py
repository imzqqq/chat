import logging

from chat.storage.engines import PostgresEngine

logger = logging.getLogger(__name__)


def run_create(cur, database_engine, *args, **kwargs):
    if isinstance(database_engine, PostgresEngine):
        cur.execute("TRUNCATE received_transactions")
    else:
        cur.execute("DELETE FROM received_transactions")

    cur.execute("CREATE INDEX received_transactions_ts ON received_transactions(ts)")


def run_upgrade(cur, database_engine, *args, **kwargs):
    pass
