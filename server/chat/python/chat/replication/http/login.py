import logging

from chat.http.servlet import parse_json_object_from_request
from chat.replication.http._base import ReplicationEndpoint

logger = logging.getLogger(__name__)


class RegisterDeviceReplicationServlet(ReplicationEndpoint):
    """Ensure a device is registered, generating a new access token for the
    device.

    Used during registration and login.
    """

    NAME = "device_check_registered"
    PATH_ARGS = ("user_id",)

    def __init__(self, hs):
        super().__init__(hs)
        self.registration_handler = hs.get_registration_handler()

    @staticmethod
    async def _serialize_payload(
        user_id,
        device_id,
        initial_display_name,
        is_guest,
        is_appservice_ghost,
        should_issue_refresh_token,
    ):
        """
        Args:
            user_id (int)
            device_id (str|None): Device ID to use, if None a new one is
                generated.
            initial_display_name (str|None)
            is_guest (bool)
            is_appservice_ghost (bool)
            should_issue_refresh_token (bool)
        """
        return {
            "device_id": device_id,
            "initial_display_name": initial_display_name,
            "is_guest": is_guest,
            "is_appservice_ghost": is_appservice_ghost,
            "should_issue_refresh_token": should_issue_refresh_token,
        }

    async def _handle_request(self, request, user_id):
        content = parse_json_object_from_request(request)

        device_id = content["device_id"]
        initial_display_name = content["initial_display_name"]
        is_guest = content["is_guest"]
        is_appservice_ghost = content["is_appservice_ghost"]
        should_issue_refresh_token = content["should_issue_refresh_token"]

        res = await self.registration_handler.register_device_inner(
            user_id,
            device_id,
            initial_display_name,
            is_guest,
            is_appservice_ghost=is_appservice_ghost,
            should_issue_refresh_token=should_issue_refresh_token,
        )

        return 200, res


def register_servlets(hs, http_server):
    RegisterDeviceReplicationServlet(hs).register(http_server)
