from chat.replication.slave.storage._base import BaseSlavedStore
from chat.replication.slave.storage._slaved_id_tracker import SlavedIdTracker
from chat.replication.tcp.streams._base import DeviceListsStream, UserSignatureStream
from chat.storage.database import DatabasePool
from chat.storage.databases.main.devices import DeviceWorkerStore
from chat.storage.databases.main.end_to_end_keys import EndToEndKeyWorkerStore
from chat.util.caches.stream_change_cache import StreamChangeCache


class SlavedDeviceStore(EndToEndKeyWorkerStore, DeviceWorkerStore, BaseSlavedStore):
    def __init__(self, database: DatabasePool, db_conn, hs):
        super().__init__(database, db_conn, hs)

        self.hs = hs

        self._device_list_id_gen = SlavedIdTracker(
            db_conn,
            "device_lists_stream",
            "stream_id",
            extra_tables=[
                ("user_signature_stream", "stream_id"),
                ("device_lists_outbound_pokes", "stream_id"),
            ],
        )
        device_list_max = self._device_list_id_gen.get_current_token()
        self._device_list_stream_cache = StreamChangeCache(
            "DeviceListStreamChangeCache", device_list_max
        )
        self._user_signature_stream_cache = StreamChangeCache(
            "UserSignatureStreamChangeCache", device_list_max
        )
        self._device_list_federation_stream_cache = StreamChangeCache(
            "DeviceListFederationStreamChangeCache", device_list_max
        )

    def get_device_stream_token(self) -> int:
        return self._device_list_id_gen.get_current_token()

    def process_replication_rows(self, stream_name, instance_name, token, rows):
        if stream_name == DeviceListsStream.NAME:
            self._device_list_id_gen.advance(instance_name, token)
            self._invalidate_caches_for_devices(token, rows)
        elif stream_name == UserSignatureStream.NAME:
            self._device_list_id_gen.advance(instance_name, token)
            for row in rows:
                self._user_signature_stream_cache.entity_has_changed(row.user_id, token)
        return super().process_replication_rows(stream_name, instance_name, token, rows)

    def _invalidate_caches_for_devices(self, token, rows):
        for row in rows:
            # The entities are either user IDs (starting with '@') whose devices
            # have changed, or remote servers that we need to tell about
            # changes.
            if row.entity.startswith("@"):
                self._device_list_stream_cache.entity_has_changed(row.entity, token)
                self.get_cached_devices_for_user.invalidate((row.entity,))
                self._get_cached_user_device.invalidate((row.entity,))
                self.get_device_list_last_stream_id_for_remote.invalidate((row.entity,))

            else:
                self._device_list_federation_stream_cache.entity_has_changed(
                    row.entity, token
                )
