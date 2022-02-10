import logging
from typing import TYPE_CHECKING, Tuple

from chat.api.errors import NotFoundError, SynapseError
from chat.http.servlet import (
    RestServlet,
    assert_params_in_dict,
    parse_json_object_from_request,
)
from chat.http.site import SynapseRequest
from chat.rest.admin._base import admin_patterns, assert_requester_is_admin
from chat.types import JsonDict, UserID

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class DeviceRestServlet(RestServlet):
    """
    Get, update or delete the given user's device
    """

    PATTERNS = admin_patterns(
        "/users/(?P<user_id>[^/]*)/devices/(?P<device_id>[^/]*)$", "v2"
    )

    def __init__(self, hs: "HomeServer"):
        super().__init__()
        self.hs = hs
        self.auth = hs.get_auth()
        self.device_handler = hs.get_device_handler()
        self.store = hs.get_datastore()

    async def on_GET(
        self, request: SynapseRequest, user_id, device_id: str
    ) -> Tuple[int, JsonDict]:
        await assert_requester_is_admin(self.auth, request)

        target_user = UserID.from_string(user_id)
        if not self.hs.is_mine(target_user):
            raise SynapseError(400, "Can only lookup local users")

        u = await self.store.get_user_by_id(target_user.to_string())
        if u is None:
            raise NotFoundError("Unknown user")

        device = await self.device_handler.get_device(
            target_user.to_string(), device_id
        )
        return 200, device

    async def on_DELETE(
        self, request: SynapseRequest, user_id: str, device_id: str
    ) -> Tuple[int, JsonDict]:
        await assert_requester_is_admin(self.auth, request)

        target_user = UserID.from_string(user_id)
        if not self.hs.is_mine(target_user):
            raise SynapseError(400, "Can only lookup local users")

        u = await self.store.get_user_by_id(target_user.to_string())
        if u is None:
            raise NotFoundError("Unknown user")

        await self.device_handler.delete_device(target_user.to_string(), device_id)
        return 200, {}

    async def on_PUT(
        self, request: SynapseRequest, user_id: str, device_id: str
    ) -> Tuple[int, JsonDict]:
        await assert_requester_is_admin(self.auth, request)

        target_user = UserID.from_string(user_id)
        if not self.hs.is_mine(target_user):
            raise SynapseError(400, "Can only lookup local users")

        u = await self.store.get_user_by_id(target_user.to_string())
        if u is None:
            raise NotFoundError("Unknown user")

        body = parse_json_object_from_request(request, allow_empty_body=True)
        await self.device_handler.update_device(
            target_user.to_string(), device_id, body
        )
        return 200, {}


class DevicesRestServlet(RestServlet):
    """
    Retrieve the given user's devices
    """

    PATTERNS = admin_patterns("/users/(?P<user_id>[^/]*)/devices$", "v2")

    def __init__(self, hs: "HomeServer"):
        """
        Args:
            hs (chat.server.HomeServer): server
        """
        self.hs = hs
        self.auth = hs.get_auth()
        self.device_handler = hs.get_device_handler()
        self.store = hs.get_datastore()

    async def on_GET(
        self, request: SynapseRequest, user_id: str
    ) -> Tuple[int, JsonDict]:
        await assert_requester_is_admin(self.auth, request)

        target_user = UserID.from_string(user_id)
        if not self.hs.is_mine(target_user):
            raise SynapseError(400, "Can only lookup local users")

        u = await self.store.get_user_by_id(target_user.to_string())
        if u is None:
            raise NotFoundError("Unknown user")

        devices = await self.device_handler.get_devices_by_user(target_user.to_string())
        return 200, {"devices": devices, "total": len(devices)}


class DeleteDevicesRestServlet(RestServlet):
    """
    API for bulk deletion of devices. Accepts a JSON object with a devices
    key which lists the device_ids to delete.
    """

    PATTERNS = admin_patterns("/users/(?P<user_id>[^/]*)/delete_devices$", "v2")

    def __init__(self, hs: "HomeServer"):
        self.hs = hs
        self.auth = hs.get_auth()
        self.device_handler = hs.get_device_handler()
        self.store = hs.get_datastore()

    async def on_POST(
        self, request: SynapseRequest, user_id: str
    ) -> Tuple[int, JsonDict]:
        await assert_requester_is_admin(self.auth, request)

        target_user = UserID.from_string(user_id)
        if not self.hs.is_mine(target_user):
            raise SynapseError(400, "Can only lookup local users")

        u = await self.store.get_user_by_id(target_user.to_string())
        if u is None:
            raise NotFoundError("Unknown user")

        body = parse_json_object_from_request(request, allow_empty_body=False)
        assert_params_in_dict(body, ["devices"])

        await self.device_handler.delete_devices(
            target_user.to_string(), body["devices"]
        )
        return 200, {}
