import logging
from typing import TYPE_CHECKING, Optional

from chat.api.errors import Codes, StoreError, SynapseError
from chat.types import Requester

from ._base import BaseHandler

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class SetPasswordHandler(BaseHandler):
    """Handler which deals with changing user account passwords"""

    def __init__(self, hs: "HomeServer"):
        super().__init__(hs)
        self._auth_handler = hs.get_auth_handler()
        self._device_handler = hs.get_device_handler()

    async def set_password(
        self,
        user_id: str,
        password_hash: str,
        logout_devices: bool,
        requester: Optional[Requester] = None,
    ) -> None:
        if not self._auth_handler.can_change_password():
            raise SynapseError(403, "Password change disabled", errcode=Codes.FORBIDDEN)

        try:
            await self.store.user_set_password_hash(user_id, password_hash)
        except StoreError as e:
            if e.code == 404:
                raise SynapseError(404, "Unknown user", Codes.NOT_FOUND)
            raise e

        # Optionally, log out all of the user's other sessions.
        if logout_devices:
            except_device_id = requester.device_id if requester else None
            except_access_token_id = requester.access_token_id if requester else None

            # First delete all of their other devices.
            await self._device_handler.delete_all_devices_for_user(
                user_id, except_device_id=except_device_id
            )

            # and now delete any access tokens which weren't associated with
            # devices (or were associated with this device).
            await self._auth_handler.delete_access_tokens_for_user(
                user_id, except_token_id=except_access_token_id
            )
