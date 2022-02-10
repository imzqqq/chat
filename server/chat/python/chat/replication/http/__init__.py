from chat.http.server import JsonResource
from chat.replication.http import (
    account_data,
    devices,
    federation,
    login,
    membership,
    presence,
    push,
    register,
    send_event,
    streams,
)

REPLICATION_PREFIX = "/_chat/replication"


class ReplicationRestResource(JsonResource):
    def __init__(self, hs):
        # We enable extracting jaeger contexts here as these are internal APIs.
        super().__init__(hs, canonical_json=False, extract_context=True)
        self.register_servlets(hs)

    def register_servlets(self, hs):
        send_event.register_servlets(hs, self)
        federation.register_servlets(hs, self)
        presence.register_servlets(hs, self)
        membership.register_servlets(hs, self)
        streams.register_servlets(hs, self)
        account_data.register_servlets(hs, self)
        push.register_servlets(hs, self)

        # The following can't currently be instantiated on workers.
        if hs.config.worker.worker_app is None:
            login.register_servlets(hs, self)
            register.register_servlets(hs, self)
            devices.register_servlets(hs, self)
