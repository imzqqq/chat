import logging

from chat.storage.database import DatabasePool
from chat.storage.databases.main.event_federation import EventFederationWorkerStore
from chat.storage.databases.main.event_push_actions import (
    EventPushActionsWorkerStore,
)
from chat.storage.databases.main.events_worker import EventsWorkerStore
from chat.storage.databases.main.relations import RelationsWorkerStore
from chat.storage.databases.main.roommember import RoomMemberWorkerStore
from chat.storage.databases.main.signatures import SignatureWorkerStore
from chat.storage.databases.main.state import StateGroupWorkerStore
from chat.storage.databases.main.stream import StreamWorkerStore
from chat.storage.databases.main.user_erasure_store import UserErasureWorkerStore
from chat.util.caches.stream_change_cache import StreamChangeCache

from ._base import BaseSlavedStore

logger = logging.getLogger(__name__)


# So, um, we want to borrow a load of functions intended for reading from
# a DataStore, but we don't want to take functions that either write to the
# DataStore or are cached and don't have cache invalidation logic.
#
# Rather than write duplicate versions of those functions, or lift them to
# a common base class, we going to grab the underlying __func__ object from
# the method descriptor on the DataStore and chuck them into our class.


class SlavedEventStore(
    EventFederationWorkerStore,
    RoomMemberWorkerStore,
    EventPushActionsWorkerStore,
    StreamWorkerStore,
    StateGroupWorkerStore,
    EventsWorkerStore,
    SignatureWorkerStore,
    UserErasureWorkerStore,
    RelationsWorkerStore,
    BaseSlavedStore,
):
    def __init__(self, database: DatabasePool, db_conn, hs):
        super().__init__(database, db_conn, hs)

        events_max = self._stream_id_gen.get_current_token()
        curr_state_delta_prefill, min_curr_state_delta_id = self.db_pool.get_cache_dict(
            db_conn,
            "current_state_delta_stream",
            entity_column="room_id",
            stream_column="stream_id",
            max_value=events_max,  # As we share the stream id with events token
            limit=1000,
        )
        self._curr_state_delta_stream_cache = StreamChangeCache(
            "_curr_state_delta_stream_cache",
            min_curr_state_delta_id,
            prefilled_cache=curr_state_delta_prefill,
        )

    # Cached functions can't be accessed through a class instance so we need
    # to reach inside the __dict__ to extract them.

    def get_room_max_stream_ordering(self):
        return self._stream_id_gen.get_current_token()

    def get_room_min_stream_ordering(self):
        return self._backfill_id_gen.get_current_token()
