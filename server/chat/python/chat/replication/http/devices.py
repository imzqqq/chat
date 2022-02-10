import logging

from chat.replication.http._base import ReplicationEndpoint

logger = logging.getLogger(__name__)


class ReplicationUserDevicesResyncRestServlet(ReplicationEndpoint):
    """Ask master to resync the device list for a user by contacting their
    server.

    This must happen on master so that the results can be correctly cached in
    the database and streamed to workers.

    Request format:

        POST /_chat/replication/user_device_resync/:user_id

        {}

    Response is equivalent to ` /chat/federation/v1/user/devices/:user_id`
    response, e.g.:

        {
            "user_id": "@alice:example.org",
            "devices": [
                {
                    "device_id": "JLAFKJWSCS",
                    "keys": { ... },
                    "device_display_name": "Alice's Mobile Phone"
                }
            ]
        }
    """

    NAME = "user_device_resync"
    PATH_ARGS = ("user_id",)
    CACHE = False

    def __init__(self, hs):
        super().__init__(hs)

        self.device_list_updater = hs.get_device_handler().device_list_updater
        self.store = hs.get_datastore()
        self.clock = hs.get_clock()

    @staticmethod
    async def _serialize_payload(user_id):
        return {}

    async def _handle_request(self, request, user_id):
        user_devices = await self.device_list_updater.user_device_resync(user_id)

        return 200, user_devices


def register_servlets(hs, http_server):
    ReplicationUserDevicesResyncRestServlet(hs).register(http_server)
