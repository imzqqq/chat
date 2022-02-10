from typing import Dict, Iterable, List, Tuple

from unpaddedbase64 import encode_base64

from chat.storage._base import SQLBaseStore
from chat.storage.types import Cursor
from chat.util.caches.descriptors import cached, cachedList


class SignatureWorkerStore(SQLBaseStore):
    @cached()
    def get_event_reference_hash(self, event_id):
        # This is a dummy function to allow get_event_reference_hashes
        # to use its cache
        raise NotImplementedError()

    @cachedList(
        cached_method_name="get_event_reference_hash", list_name="event_ids", num_args=1
    )
    async def get_event_reference_hashes(
        self, event_ids: Iterable[str]
    ) -> Dict[str, Dict[str, bytes]]:
        """Get all hashes for given events.

        Args:
            event_ids: The event IDs to get hashes for.

        Returns:
             A mapping of event ID to a mapping of algorithm to hash.
        """

        def f(txn):
            return {
                event_id: self._get_event_reference_hashes_txn(txn, event_id)
                for event_id in event_ids
            }

        return await self.db_pool.runInteraction("get_event_reference_hashes", f)

    async def add_event_hashes(
        self, event_ids: Iterable[str]
    ) -> List[Tuple[str, Dict[str, str]]]:
        """

        Args:
            event_ids: The event IDs

        Returns:
            A list of tuples of event ID and a mapping of algorithm to base-64 encoded hash.
        """
        hashes = await self.get_event_reference_hashes(event_ids)
        hashes = {
            e_id: {k: encode_base64(v) for k, v in h.items() if k == "sha256"}
            for e_id, h in hashes.items()
        }

        return list(hashes.items())

    def _get_event_reference_hashes_txn(
        self, txn: Cursor, event_id: str
    ) -> Dict[str, bytes]:
        """Get all the hashes for a given PDU.
        Args:
            txn:
            event_id: Id for the Event.
        Returns:
            A mapping of algorithm -> hash.
        """
        query = (
            "SELECT algorithm, hash"
            " FROM event_reference_hashes"
            " WHERE event_id = ?"
        )
        txn.execute(query, (event_id,))
        return {k: v for k, v in txn}


class SignatureStore(SignatureWorkerStore):
    """Persistence for event signatures and hashes"""
