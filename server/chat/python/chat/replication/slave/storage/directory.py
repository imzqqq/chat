from chat.storage.databases.main.directory import DirectoryWorkerStore

from ._base import BaseSlavedStore


class DirectoryStore(DirectoryWorkerStore, BaseSlavedStore):
    pass
