import logging
from typing import TYPE_CHECKING

from chat.http.servlet import parse_json_object_from_request
from chat.replication.http._base import ReplicationEndpoint
from chat.types import UserID

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class ReplicationBumpPresenceActiveTime(ReplicationEndpoint):
    """We've seen the user do something that indicates they're interacting
    with the app.

    The POST looks like:

        POST /_chat/replication/bump_presence_active_time/<user_id>

        200 OK

        {}
    """

    NAME = "bump_presence_active_time"
    PATH_ARGS = ("user_id",)
    METHOD = "POST"
    CACHE = False

    def __init__(self, hs: "HomeServer"):
        super().__init__(hs)

        self._presence_handler = hs.get_presence_handler()

    @staticmethod
    async def _serialize_payload(user_id):
        return {}

    async def _handle_request(self, request, user_id):
        await self._presence_handler.bump_presence_active_time(
            UserID.from_string(user_id)
        )

        return (
            200,
            {},
        )


class ReplicationPresenceSetState(ReplicationEndpoint):
    """Set the presence state for a user.

    The POST looks like:

        POST /_chat/replication/presence_set_state/<user_id>

        {
            "state": { ... },
            "ignore_status_msg": false,
            "force_notify": false
        }

        200 OK

        {}
    """

    NAME = "presence_set_state"
    PATH_ARGS = ("user_id",)
    METHOD = "POST"
    CACHE = False

    def __init__(self, hs: "HomeServer"):
        super().__init__(hs)

        self._presence_handler = hs.get_presence_handler()

    @staticmethod
    async def _serialize_payload(
        user_id, state, ignore_status_msg=False, force_notify=False
    ):
        return {
            "state": state,
            "ignore_status_msg": ignore_status_msg,
            "force_notify": force_notify,
        }

    async def _handle_request(self, request, user_id):
        content = parse_json_object_from_request(request)

        await self._presence_handler.set_state(
            UserID.from_string(user_id),
            content["state"],
            content["ignore_status_msg"],
            content["force_notify"],
        )

        return (
            200,
            {},
        )


def register_servlets(hs, http_server):
    ReplicationBumpPresenceActiveTime(hs).register(http_server)
    ReplicationPresenceSetState(hs).register(http_server)
