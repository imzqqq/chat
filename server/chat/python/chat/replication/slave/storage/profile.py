from chat.replication.slave.storage._base import BaseSlavedStore
from chat.storage.databases.main.profile import ProfileWorkerStore


class SlavedProfileStore(ProfileWorkerStore, BaseSlavedStore):
    pass
