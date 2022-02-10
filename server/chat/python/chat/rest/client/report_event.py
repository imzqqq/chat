import logging
from http import HTTPStatus

from chat.api.errors import Codes, SynapseError
from chat.http.servlet import RestServlet, parse_json_object_from_request

from ._base import client_patterns

logger = logging.getLogger(__name__)


class ReportEventRestServlet(RestServlet):
    PATTERNS = client_patterns("/rooms/(?P<room_id>[^/]*)/report/(?P<event_id>[^/]*)$")

    def __init__(self, hs):
        super().__init__()
        self.hs = hs
        self.auth = hs.get_auth()
        self.clock = hs.get_clock()
        self.store = hs.get_datastore()

    async def on_POST(self, request, room_id, event_id):
        requester = await self.auth.get_user_by_req(request)
        user_id = requester.user.to_string()

        body = parse_json_object_from_request(request)

        if not isinstance(body.get("reason", ""), str):
            raise SynapseError(
                HTTPStatus.BAD_REQUEST,
                "Param 'reason' must be a string",
                Codes.BAD_JSON,
            )
        if not isinstance(body.get("score", 0), int):
            raise SynapseError(
                HTTPStatus.BAD_REQUEST,
                "Param 'score' must be an integer",
                Codes.BAD_JSON,
            )

        await self.store.add_event_report(
            room_id=room_id,
            event_id=event_id,
            user_id=user_id,
            reason=body.get("reason"),
            content=body,
            received_ts=self.clock.time_msec(),
        )

        return 200, {}


def register_servlets(hs, http_server):
    ReportEventRestServlet(hs).register(http_server)
