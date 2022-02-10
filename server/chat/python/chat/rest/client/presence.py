""" This module contains REST servlets to do with presence: /presence/<paths>
"""
import logging

from chat.api.errors import AuthError, SynapseError
from chat.handlers.presence import format_user_presence_state
from chat.http.servlet import RestServlet, parse_json_object_from_request
from chat.rest.client._base import client_patterns
from chat.types import UserID

logger = logging.getLogger(__name__)


class PresenceStatusRestServlet(RestServlet):
    PATTERNS = client_patterns("/presence/(?P<user_id>[^/]*)/status", v1=True)

    def __init__(self, hs):
        super().__init__()
        self.hs = hs
        self.presence_handler = hs.get_presence_handler()
        self.clock = hs.get_clock()
        self.auth = hs.get_auth()

        self._use_presence = hs.config.server.use_presence

    async def on_GET(self, request, user_id):
        requester = await self.auth.get_user_by_req(request)
        user = UserID.from_string(user_id)

        if not self._use_presence:
            return 200, {"presence": "offline"}

        if requester.user != user:
            allowed = await self.presence_handler.is_visible(
                observed_user=user, observer_user=requester.user
            )

            if not allowed:
                raise AuthError(403, "You are not allowed to see their presence.")

        state = await self.presence_handler.get_state(target_user=user)
        state = format_user_presence_state(
            state, self.clock.time_msec(), include_user_id=False
        )

        return 200, state

    async def on_PUT(self, request, user_id):
        requester = await self.auth.get_user_by_req(request)
        user = UserID.from_string(user_id)

        if requester.user != user:
            raise AuthError(403, "Can only set your own presence state")

        state = {}

        content = parse_json_object_from_request(request)

        try:
            state["presence"] = content.pop("presence")

            if "status_msg" in content:
                state["status_msg"] = content.pop("status_msg")
                if not isinstance(state["status_msg"], str):
                    raise SynapseError(400, "status_msg must be a string.")

            if content:
                raise KeyError()
        except SynapseError as e:
            raise e
        except Exception:
            raise SynapseError(400, "Unable to parse state")

        if self._use_presence:
            await self.presence_handler.set_state(user, state)

        return 200, {}


def register_servlets(hs, http_server):
    PresenceStatusRestServlet(hs).register(http_server)
