import logging

from chat.api.errors import Codes, SynapseError
from chat.http.servlet import RestServlet
from chat.types import UserID

from ._base import client_patterns

logger = logging.getLogger(__name__)


class UserSharedRoomsServlet(RestServlet):
    """
    GET /uk.half-shot.msc2666/user/shared_rooms/{user_id} HTTP/1.1
    """

    PATTERNS = client_patterns(
        "/uk.half-shot.msc2666/user/shared_rooms/(?P<user_id>[^/]*)",
        releases=(),  # This is an unstable feature
    )

    def __init__(self, hs):
        super().__init__()
        self.auth = hs.get_auth()
        self.store = hs.get_datastore()
        self.user_directory_active = hs.config.update_user_directory

    async def on_GET(self, request, user_id):

        if not self.user_directory_active:
            raise SynapseError(
                code=400,
                msg="The user directory is disabled on this server. Cannot determine shared rooms.",
                errcode=Codes.FORBIDDEN,
            )

        UserID.from_string(user_id)

        requester = await self.auth.get_user_by_req(request)
        if user_id == requester.user.to_string():
            raise SynapseError(
                code=400,
                msg="You cannot request a list of shared rooms with yourself",
                errcode=Codes.FORBIDDEN,
            )
        rooms = await self.store.get_shared_rooms_for_users(
            requester.user.to_string(), user_id
        )

        return 200, {"joined": list(rooms)}


def register_servlets(hs, http_server):
    UserSharedRoomsServlet(hs).register(http_server)
