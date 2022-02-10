from chat.storage.databases.main.receipts import ReceiptsWorkerStore

from ._base import BaseSlavedStore


class SlavedReceiptsStore(ReceiptsWorkerStore, BaseSlavedStore):
    pass
