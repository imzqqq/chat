import logging

from chat.api.errors import AuthError, NotFoundError, SynapseError
from chat.http.servlet import RestServlet, parse_json_object_from_request

from ._base import client_patterns

logger = logging.getLogger(__name__)


class AccountDataServlet(RestServlet):
    """
    PUT /user/{user_id}/account_data/{account_dataType} HTTP/1.1
    GET /user/{user_id}/account_data/{account_dataType} HTTP/1.1
    """

    PATTERNS = client_patterns(
        "/user/(?P<user_id>[^/]*)/account_data/(?P<account_data_type>[^/]*)"
    )

    def __init__(self, hs):
        super().__init__()
        self.auth = hs.get_auth()
        self.store = hs.get_datastore()
        self.handler = hs.get_account_data_handler()

    async def on_PUT(self, request, user_id, account_data_type):
        requester = await self.auth.get_user_by_req(request)
        if user_id != requester.user.to_string():
            raise AuthError(403, "Cannot add account data for other users.")

        body = parse_json_object_from_request(request)

        await self.handler.add_account_data_for_user(user_id, account_data_type, body)

        return 200, {}

    async def on_GET(self, request, user_id, account_data_type):
        requester = await self.auth.get_user_by_req(request)
        if user_id != requester.user.to_string():
            raise AuthError(403, "Cannot get account data for other users.")

        event = await self.store.get_global_account_data_by_type_for_user(
            account_data_type, user_id
        )

        if event is None:
            raise NotFoundError("Account data not found")

        return 200, event


class RoomAccountDataServlet(RestServlet):
    """
    PUT /user/{user_id}/rooms/{room_id}/account_data/{account_dataType} HTTP/1.1
    GET /user/{user_id}/rooms/{room_id}/account_data/{account_dataType} HTTP/1.1
    """

    PATTERNS = client_patterns(
        "/user/(?P<user_id>[^/]*)"
        "/rooms/(?P<room_id>[^/]*)"
        "/account_data/(?P<account_data_type>[^/]*)"
    )

    def __init__(self, hs):
        super().__init__()
        self.auth = hs.get_auth()
        self.store = hs.get_datastore()
        self.handler = hs.get_account_data_handler()

    async def on_PUT(self, request, user_id, room_id, account_data_type):
        requester = await self.auth.get_user_by_req(request)
        if user_id != requester.user.to_string():
            raise AuthError(403, "Cannot add account data for other users.")

        body = parse_json_object_from_request(request)

        if account_data_type == "m.fully_read":
            raise SynapseError(
                405,
                "Cannot set m.fully_read through this API."
                " Use /rooms/!roomId:server.name/read_markers",
            )

        await self.handler.add_account_data_to_room(
            user_id, room_id, account_data_type, body
        )

        return 200, {}

    async def on_GET(self, request, user_id, room_id, account_data_type):
        requester = await self.auth.get_user_by_req(request)
        if user_id != requester.user.to_string():
            raise AuthError(403, "Cannot get account data for other users.")

        event = await self.store.get_account_data_for_room_and_type(
            user_id, room_id, account_data_type
        )

        if event is None:
            raise NotFoundError("Room account data not found")

        return 200, event


def register_servlets(hs, http_server):
    AccountDataServlet(hs).register(http_server)
    RoomAccountDataServlet(hs).register(http_server)
