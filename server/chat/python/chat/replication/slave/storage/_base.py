import logging
from typing import Optional

from chat.storage.database import DatabasePool
from chat.storage.databases.main.cache import CacheInvalidationWorkerStore
from chat.storage.engines import PostgresEngine
from chat.storage.util.id_generators import MultiWriterIdGenerator

logger = logging.getLogger(__name__)


class BaseSlavedStore(CacheInvalidationWorkerStore):
    def __init__(self, database: DatabasePool, db_conn, hs):
        super().__init__(database, db_conn, hs)
        if isinstance(self.database_engine, PostgresEngine):
            self._cache_id_gen: Optional[
                MultiWriterIdGenerator
            ] = MultiWriterIdGenerator(
                db_conn,
                database,
                stream_name="caches",
                instance_name=hs.get_instance_name(),
                tables=[
                    (
                        "cache_invalidation_stream_by_instance",
                        "instance_name",
                        "stream_id",
                    )
                ],
                sequence_name="cache_invalidation_stream_seq",
                writers=[],
            )
        else:
            self._cache_id_gen = None

        self.hs = hs
