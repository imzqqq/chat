from typing import TYPE_CHECKING

from chat.replication.tcp.streams import PushersStream
from chat.storage.database import DatabasePool
from chat.storage.databases.main.pusher import PusherWorkerStore
from chat.storage.types import Connection

from ._base import BaseSlavedStore
from ._slaved_id_tracker import SlavedIdTracker

if TYPE_CHECKING:
    from chat.server import HomeServer


class SlavedPusherStore(PusherWorkerStore, BaseSlavedStore):
    def __init__(self, database: DatabasePool, db_conn: Connection, hs: "HomeServer"):
        super().__init__(database, db_conn, hs)
        self._pushers_id_gen = SlavedIdTracker(  # type: ignore
            db_conn, "pushers", "id", extra_tables=[("deleted_pushers", "stream_id")]
        )

    def get_pushers_stream_token(self) -> int:
        return self._pushers_id_gen.get_current_token()

    def process_replication_rows(
        self, stream_name: str, instance_name: str, token, rows
    ) -> None:
        if stream_name == PushersStream.NAME:
            self._pushers_id_gen.advance(instance_name, token)  # type: ignore
        return super().process_replication_rows(stream_name, instance_name, token, rows)
