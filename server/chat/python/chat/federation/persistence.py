""" This module contains all the persistence actions done by the federation
package.

These actions are mostly only used by the :py:mod:`.replication` module.
"""

import logging
from typing import Optional, Tuple

from chat.federation.units import Transaction
from chat.logging.utils import log_function
from chat.types import JsonDict

logger = logging.getLogger(__name__)


class TransactionActions:
    """Defines persistence actions that relate to handling Transactions."""

    def __init__(self, datastore):
        self.store = datastore

    @log_function
    async def have_responded(
        self, origin: str, transaction: Transaction
    ) -> Optional[Tuple[int, JsonDict]]:
        """Have we already responded to a transaction with the same id and
        origin?

        Returns:
            `None` if we have not previously responded to this transaction or a
            2-tuple of `(int, dict)` representing the response code and response body.
        """
        transaction_id = transaction.transaction_id
        if not transaction_id:
            raise RuntimeError("Cannot persist a transaction with no transaction_id")

        return await self.store.get_received_txn_response(transaction_id, origin)

    @log_function
    async def set_response(
        self, origin: str, transaction: Transaction, code: int, response: JsonDict
    ) -> None:
        """Persist how we responded to a transaction."""
        transaction_id = transaction.transaction_id
        if not transaction_id:
            raise RuntimeError("Cannot persist a transaction with no transaction_id")

        await self.store.set_received_txn_response(
            transaction_id, origin, code, response
        )
