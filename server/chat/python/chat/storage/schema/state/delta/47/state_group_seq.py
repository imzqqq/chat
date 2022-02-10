from chat.storage.engines import PostgresEngine


def run_create(cur, database_engine, *args, **kwargs):
    if isinstance(database_engine, PostgresEngine):
        # if we already have some state groups, we want to start making new
        # ones with a higher id.
        cur.execute("SELECT max(id) FROM state_groups")
        row = cur.fetchone()

        if row[0] is None:
            start_val = 1
        else:
            start_val = row[0] + 1

        cur.execute("CREATE SEQUENCE state_group_id_seq START WITH %s", (start_val,))


def run_upgrade(*args, **kwargs):
    pass
