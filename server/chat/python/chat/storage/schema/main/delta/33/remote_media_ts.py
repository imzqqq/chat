import time

ALTER_TABLE = "ALTER TABLE remote_media_cache ADD COLUMN last_access_ts BIGINT"


def run_create(cur, database_engine, *args, **kwargs):
    cur.execute(ALTER_TABLE)


def run_upgrade(cur, database_engine, *args, **kwargs):
    cur.execute(
        "UPDATE remote_media_cache SET last_access_ts = ?",
        (int(time.time() * 1000),),
    )
