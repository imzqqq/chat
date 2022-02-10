# -*- coding: utf-8 -*-

from typing import TYPE_CHECKING

import chat.util.stringutils as stringutils
from chat.api.errors import StoreError
from chat.metrics.background_process_metrics import wrap_as_background_process
from chat.storage._base import SQLBaseStore, db_to_json
from chat.storage.database import (
    DatabasePool,
    LoggingDatabaseConnection,
    LoggingTransaction,
)
from chat.types import JsonDict
from chat.util import json_encoder

if TYPE_CHECKING:
    from chat.server import HomeServer


class SessionStore(SQLBaseStore):
    """
    A store for generic session data.

    Each type of session should provide a unique type (to separate sessions).

    Sessions are automatically removed when they expire.
    """

    def __init__(
        self,
        database: DatabasePool,
        db_conn: LoggingDatabaseConnection,
        hs: "HomeServer",
    ):
        super().__init__(database, db_conn, hs)

        # Create a background job for culling expired sessions.
        if hs.config.run_background_tasks:
            self._clock.looping_call(self._delete_expired_sessions, 30 * 60 * 1000)

    async def create_session(
        self, session_type: str, value: JsonDict, expiry_ms: int
    ) -> str:
        """
        Creates a new pagination session for the room hierarchy endpoint.

        Args:
            session_type: The type for this session.
            value: The value to store.
            expiry_ms: How long before an item is evicted from the cache
                in milliseconds. Default is 0, indicating items never get
                evicted based on time.

        Returns:
            The newly created session ID.

        Raises:
            StoreError if a unique session ID cannot be generated.
        """
        # autogen a session ID and try to create it. We may clash, so just
        # try a few times till one goes through, giving up eventually.
        attempts = 0
        while attempts < 5:
            session_id = stringutils.random_string(24)

            try:
                await self.db_pool.simple_insert(
                    table="sessions",
                    values={
                        "session_id": session_id,
                        "session_type": session_type,
                        "value": json_encoder.encode(value),
                        "expiry_time_ms": self.hs.get_clock().time_msec() + expiry_ms,
                    },
                    desc="create_session",
                )

                return session_id
            except self.db_pool.engine.module.IntegrityError:
                attempts += 1
        raise StoreError(500, "Couldn't generate a session ID.")

    async def get_session(self, session_type: str, session_id: str) -> JsonDict:
        """
        Retrieve data stored with create_session

        Args:
            session_type: The type for this session.
            session_id: The session ID returned from create_session.

        Raises:
            StoreError if the session cannot be found.
        """

        def _get_session(
            txn: LoggingTransaction, session_type: str, session_id: str, ts: int
        ) -> JsonDict:
            # This includes the expiry time since items are only periodically
            # deleted, not upon expiry.
            select_sql = """
            SELECT value FROM sessions WHERE
            session_type = ? AND session_id = ? AND expiry_time_ms > ?
            """
            txn.execute(select_sql, [session_type, session_id, ts])
            row = txn.fetchone()

            if not row:
                raise StoreError(404, "No session")

            return db_to_json(row[0])

        return await self.db_pool.runInteraction(
            "get_session",
            _get_session,
            session_type,
            session_id,
            self._clock.time_msec(),
        )

    @wrap_as_background_process("delete_expired_sessions")
    async def _delete_expired_sessions(self) -> None:
        """Remove sessions with expiry dates that have passed."""

        def _delete_expired_sessions_txn(txn: LoggingTransaction, ts: int) -> None:
            sql = "DELETE FROM sessions WHERE expiry_time_ms <= ?"
            txn.execute(sql, (ts,))

        await self.db_pool.runInteraction(
            "delete_expired_sessions",
            _delete_expired_sessions_txn,
            self._clock.time_msec(),
        )
