import logging

from chat.http.servlet import RestServlet
from chat.rest.client._base import client_patterns

logger = logging.getLogger(__name__)


class LogoutRestServlet(RestServlet):
    PATTERNS = client_patterns("/logout$", v1=True)

    def __init__(self, hs):
        super().__init__()
        self.auth = hs.get_auth()
        self._auth_handler = hs.get_auth_handler()
        self._device_handler = hs.get_device_handler()

    async def on_POST(self, request):
        requester = await self.auth.get_user_by_req(request, allow_expired=True)

        if requester.device_id is None:
            # The access token wasn't associated with a device.
            # Just delete the access token
            access_token = self.auth.get_access_token_from_request(request)
            await self._auth_handler.delete_access_token(access_token)
        else:
            await self._device_handler.delete_device(
                requester.user.to_string(), requester.device_id
            )

        return 200, {}


class LogoutAllRestServlet(RestServlet):
    PATTERNS = client_patterns("/logout/all$", v1=True)

    def __init__(self, hs):
        super().__init__()
        self.auth = hs.get_auth()
        self._auth_handler = hs.get_auth_handler()
        self._device_handler = hs.get_device_handler()

    async def on_POST(self, request):
        requester = await self.auth.get_user_by_req(request, allow_expired=True)
        user_id = requester.user.to_string()

        # first delete all of the user's devices
        await self._device_handler.delete_all_devices_for_user(user_id)

        # .. and then delete any access tokens which weren't associated with
        # devices.
        await self._auth_handler.delete_access_tokens_for_user(user_id)
        return 200, {}


def register_servlets(hs, http_server):
    LogoutRestServlet(hs).register(http_server)
    LogoutAllRestServlet(hs).register(http_server)
