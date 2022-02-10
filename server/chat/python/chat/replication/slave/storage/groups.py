from chat.replication.slave.storage._base import BaseSlavedStore
from chat.replication.slave.storage._slaved_id_tracker import SlavedIdTracker
from chat.replication.tcp.streams import GroupServerStream
from chat.storage.database import DatabasePool
from chat.storage.databases.main.group_server import GroupServerWorkerStore
from chat.util.caches.stream_change_cache import StreamChangeCache


class SlavedGroupServerStore(GroupServerWorkerStore, BaseSlavedStore):
    def __init__(self, database: DatabasePool, db_conn, hs):
        super().__init__(database, db_conn, hs)

        self.hs = hs

        self._group_updates_id_gen = SlavedIdTracker(
            db_conn, "local_group_updates", "stream_id"
        )
        self._group_updates_stream_cache = StreamChangeCache(
            "_group_updates_stream_cache",
            self._group_updates_id_gen.get_current_token(),
        )

    def get_group_stream_token(self):
        return self._group_updates_id_gen.get_current_token()

    def process_replication_rows(self, stream_name, instance_name, token, rows):
        if stream_name == GroupServerStream.NAME:
            self._group_updates_id_gen.advance(instance_name, token)
            for row in rows:
                self._group_updates_stream_cache.entity_has_changed(row.user_id, token)

        return super().process_replication_rows(stream_name, instance_name, token, rows)
