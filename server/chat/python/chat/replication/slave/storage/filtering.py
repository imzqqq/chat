from chat.storage.database import DatabasePool
from chat.storage.databases.main.filtering import FilteringStore

from ._base import BaseSlavedStore


class SlavedFilteringStore(BaseSlavedStore):
    def __init__(self, database: DatabasePool, db_conn, hs):
        super().__init__(database, db_conn, hs)

    # Filters are immutable so this cache doesn't need to be expired
    get_user_filter = FilteringStore.__dict__["get_user_filter"]
