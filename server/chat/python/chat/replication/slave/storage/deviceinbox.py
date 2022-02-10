from chat.replication.slave.storage._base import BaseSlavedStore
from chat.storage.databases.main.deviceinbox import DeviceInboxWorkerStore


class SlavedDeviceInboxStore(DeviceInboxWorkerStore, BaseSlavedStore):
    pass
