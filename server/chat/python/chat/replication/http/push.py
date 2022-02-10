import logging
from typing import TYPE_CHECKING

from chat.http.servlet import parse_json_object_from_request
from chat.replication.http._base import ReplicationEndpoint

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class ReplicationRemovePusherRestServlet(ReplicationEndpoint):
    """Deletes the given pusher.

    Request format:

        POST /_chat/replication/remove_pusher/:user_id

        {
            "app_id": "<some_id>",
            "pushkey": "<some_key>"
        }

    """

    NAME = "add_user_account_data"
    PATH_ARGS = ("user_id",)
    CACHE = False

    def __init__(self, hs: "HomeServer"):
        super().__init__(hs)

        self.pusher_pool = hs.get_pusherpool()

    @staticmethod
    async def _serialize_payload(app_id, pushkey, user_id):
        payload = {
            "app_id": app_id,
            "pushkey": pushkey,
        }

        return payload

    async def _handle_request(self, request, user_id):
        content = parse_json_object_from_request(request)

        app_id = content["app_id"]
        pushkey = content["pushkey"]

        await self.pusher_pool.remove_pusher(app_id, pushkey, user_id)

        return 200, {}


def register_servlets(hs, http_server):
    ReplicationRemovePusherRestServlet(hs).register(http_server)
