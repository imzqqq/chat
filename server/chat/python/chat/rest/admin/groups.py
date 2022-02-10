import logging
from typing import TYPE_CHECKING, Tuple

from chat.api.errors import SynapseError
from chat.http.servlet import RestServlet
from chat.http.site import SynapseRequest
from chat.rest.admin._base import admin_patterns, assert_user_is_admin
from chat.types import JsonDict

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class DeleteGroupAdminRestServlet(RestServlet):
    """Allows deleting of local groups"""

    PATTERNS = admin_patterns("/delete_group/(?P<group_id>[^/]*)")

    def __init__(self, hs: "HomeServer"):
        self.group_server = hs.get_groups_server_handler()
        self.is_mine_id = hs.is_mine_id
        self.auth = hs.get_auth()

    async def on_POST(
        self, request: SynapseRequest, group_id: str
    ) -> Tuple[int, JsonDict]:
        requester = await self.auth.get_user_by_req(request)
        await assert_user_is_admin(self.auth, requester.user)

        if not self.is_mine_id(group_id):
            raise SynapseError(400, "Can only delete local groups")

        await self.group_server.delete_group(group_id, requester.user.to_string())
        return 200, {}
