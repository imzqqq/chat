import logging

from chat.api.errors import Codes, ShadowBanError, SynapseError
from chat.api.room_versions import KNOWN_ROOM_VERSIONS
from chat.http.servlet import (
    RestServlet,
    assert_params_in_dict,
    parse_json_object_from_request,
)
from chat.util import stringutils

from ._base import client_patterns

logger = logging.getLogger(__name__)


class RoomUpgradeRestServlet(RestServlet):
    """Handler for room upgrade requests.

    Handles requests of the form:

        POST /chat/client/r0/rooms/$roomid/upgrade HTTP/1.1
        Content-Type: application/json

        {
            "new_version": "2",
        }

    Creates a new room and shuts down the old one. Returns the ID of the new room.

    Args:
        hs (chat.server.HomeServer):
    """

    PATTERNS = client_patterns(
        # /rooms/$roomid/upgrade
        "/rooms/(?P<room_id>[^/]*)/upgrade$"
    )

    def __init__(self, hs):
        super().__init__()
        self._hs = hs
        self._room_creation_handler = hs.get_room_creation_handler()
        self._auth = hs.get_auth()

    async def on_POST(self, request, room_id):
        requester = await self._auth.get_user_by_req(request)

        content = parse_json_object_from_request(request)
        assert_params_in_dict(content, ("new_version",))

        new_version = KNOWN_ROOM_VERSIONS.get(content["new_version"])
        if new_version is None:
            raise SynapseError(
                400,
                "Your homeserver does not support this room version",
                Codes.UNSUPPORTED_ROOM_VERSION,
            )

        try:
            new_room_id = await self._room_creation_handler.upgrade_room(
                requester, room_id, new_version
            )
        except ShadowBanError:
            # Generate a random room ID.
            new_room_id = stringutils.random_string(18)

        ret = {"replacement_room": new_room_id}

        return 200, ret


def register_servlets(hs, http_server):
    RoomUpgradeRestServlet(hs).register(http_server)
