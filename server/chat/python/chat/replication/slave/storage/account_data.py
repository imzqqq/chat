from chat.replication.slave.storage._base import BaseSlavedStore
from chat.storage.databases.main.account_data import AccountDataWorkerStore
from chat.storage.databases.main.tags import TagsWorkerStore


class SlavedAccountDataStore(TagsWorkerStore, AccountDataWorkerStore, BaseSlavedStore):
    pass
