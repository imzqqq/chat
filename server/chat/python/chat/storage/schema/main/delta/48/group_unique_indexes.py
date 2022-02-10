from chat.storage.engines import PostgresEngine
from chat.storage.prepare_database import get_statements

FIX_INDEXES = """
-- rebuild indexes as uniques
DROP INDEX groups_invites_g_idx;
CREATE UNIQUE INDEX group_invites_g_idx ON group_invites(group_id, user_id);
DROP INDEX groups_users_g_idx;
CREATE UNIQUE INDEX group_users_g_idx ON group_users(group_id, user_id);

-- rename other indexes to actually match their table names..
DROP INDEX groups_users_u_idx;
CREATE INDEX group_users_u_idx ON group_users(user_id);
DROP INDEX groups_invites_u_idx;
CREATE INDEX group_invites_u_idx ON group_invites(user_id);
DROP INDEX groups_rooms_g_idx;
CREATE UNIQUE INDEX group_rooms_g_idx ON group_rooms(group_id, room_id);
DROP INDEX groups_rooms_r_idx;
CREATE INDEX group_rooms_r_idx ON group_rooms(room_id);
"""


def run_create(cur, database_engine, *args, **kwargs):
    rowid = "ctid" if isinstance(database_engine, PostgresEngine) else "rowid"

    # remove duplicates from group_users & group_invites tables
    cur.execute(
        """
        DELETE FROM group_users WHERE %s NOT IN (
           SELECT min(%s) FROM group_users GROUP BY group_id, user_id
        );
    """
        % (rowid, rowid)
    )
    cur.execute(
        """
        DELETE FROM group_invites WHERE %s NOT IN (
           SELECT min(%s) FROM group_invites GROUP BY group_id, user_id
        );
    """
        % (rowid, rowid)
    )

    for statement in get_statements(FIX_INDEXES.splitlines()):
        cur.execute(statement)


def run_upgrade(*args, **kwargs):
    pass
