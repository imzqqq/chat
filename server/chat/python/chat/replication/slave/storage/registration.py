from chat.storage.databases.main.registration import RegistrationWorkerStore

from ._base import BaseSlavedStore


class SlavedRegistrationStore(RegistrationWorkerStore, BaseSlavedStore):
    pass
