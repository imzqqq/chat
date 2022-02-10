from chat.storage.database import DatabasePool
from chat.storage.databases.main.client_ips import LAST_SEEN_GRANULARITY
from chat.util.caches.lrucache import LruCache

from ._base import BaseSlavedStore


class SlavedClientIpStore(BaseSlavedStore):
    def __init__(self, database: DatabasePool, db_conn, hs):
        super().__init__(database, db_conn, hs)

        self.client_ip_last_seen: LruCache[tuple, int] = LruCache(
            cache_name="client_ip_last_seen", max_size=50000
        )

    async def insert_client_ip(self, user_id, access_token, ip, user_agent, device_id):
        now = int(self._clock.time_msec())
        key = (user_id, access_token, ip)

        try:
            last_seen = self.client_ip_last_seen.get(key)
        except KeyError:
            last_seen = None

        # Rate-limited inserts
        if last_seen is not None and (now - last_seen) < LAST_SEEN_GRANULARITY:
            return

        self.client_ip_last_seen.set(key, now)

        self.hs.get_tcp_replication().send_user_ip(
            user_id, access_token, ip, user_agent, device_id, now
        )
