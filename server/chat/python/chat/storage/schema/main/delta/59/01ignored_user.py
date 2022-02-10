"""
This migration denormalises the account_data table into an ignored users table.
"""

import logging
from io import StringIO

from chat.storage._base import db_to_json
from chat.storage.engines import BaseDatabaseEngine
from chat.storage.prepare_database import execute_statements_from_stream
from chat.storage.types import Cursor

logger = logging.getLogger(__name__)


def run_upgrade(cur: Cursor, database_engine: BaseDatabaseEngine, *args, **kwargs):
    pass


def run_create(cur: Cursor, database_engine: BaseDatabaseEngine, *args, **kwargs):
    logger.info("Creating ignored_users table")
    execute_statements_from_stream(cur, StringIO(_create_commands))

    # We now upgrade existing data, if any. We don't do this in `run_upgrade` as
    # we a) want to run these before adding constraints and b) `run_upgrade` is
    # not run on empty databases.
    insert_sql = """
    INSERT INTO ignored_users (ignorer_user_id, ignored_user_id) VALUES (?, ?)
    """

    logger.info("Converting existing ignore lists")
    cur.execute(
        "SELECT user_id, content FROM account_data WHERE account_data_type = 'm.ignored_user_list'"
    )
    for user_id, content_json in cur.fetchall():
        content = db_to_json(content_json)

        # The content should be the form of a dictionary with a key
        # "ignored_users" pointing to a dictionary with keys of ignored users.
        #
        # { "ignored_users": "@someone:example.org": {} }
        ignored_users = content.get("ignored_users", {})
        if isinstance(ignored_users, dict) and ignored_users:
            cur.execute_batch(insert_sql, [(user_id, u) for u in ignored_users])

    # Add indexes after inserting data for efficiency.
    logger.info("Adding constraints to ignored_users table")
    execute_statements_from_stream(cur, StringIO(_constraints_commands))


# there might be duplicates, so the easiest way to achieve this is to create a new
# table with the right data, and renaming it into place

_create_commands = """
-- Users which are ignored when calculating push notifications. This data is
-- denormalized from account data.
CREATE TABLE IF NOT EXISTS ignored_users(
    ignorer_user_id TEXT NOT NULL,  -- The user ID of the user who is ignoring another user. (This is a local user.)
    ignored_user_id TEXT NOT NULL  -- The user ID of the user who is being ignored. (This is a local or remote user.)
);
"""

_constraints_commands = """
CREATE UNIQUE INDEX ignored_users_uniqueness ON ignored_users (ignorer_user_id, ignored_user_id);

-- Add an index on ignored_users since look-ups are done to get all ignorers of an ignored user.
CREATE INDEX ignored_users_ignored_user_id ON ignored_users (ignored_user_id);
"""
