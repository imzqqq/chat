import logging

from chat.http.servlet import parse_json_object_from_request
from chat.replication.http._base import ReplicationEndpoint

logger = logging.getLogger(__name__)


class ReplicationUserAccountDataRestServlet(ReplicationEndpoint):
    """Add user account data on the appropriate account data worker.

    Request format:

        POST /_chat/replication/add_user_account_data/:user_id/:type

        {
            "content": { ... },
        }

    """

    NAME = "add_user_account_data"
    PATH_ARGS = ("user_id", "account_data_type")
    CACHE = False

    def __init__(self, hs):
        super().__init__(hs)

        self.handler = hs.get_account_data_handler()
        self.clock = hs.get_clock()

    @staticmethod
    async def _serialize_payload(user_id, account_data_type, content):
        payload = {
            "content": content,
        }

        return payload

    async def _handle_request(self, request, user_id, account_data_type):
        content = parse_json_object_from_request(request)

        max_stream_id = await self.handler.add_account_data_for_user(
            user_id, account_data_type, content["content"]
        )

        return 200, {"max_stream_id": max_stream_id}


class ReplicationRoomAccountDataRestServlet(ReplicationEndpoint):
    """Add room account data on the appropriate account data worker.

    Request format:

        POST /_chat/replication/add_room_account_data/:user_id/:room_id/:account_data_type

        {
            "content": { ... },
        }

    """

    NAME = "add_room_account_data"
    PATH_ARGS = ("user_id", "room_id", "account_data_type")
    CACHE = False

    def __init__(self, hs):
        super().__init__(hs)

        self.handler = hs.get_account_data_handler()
        self.clock = hs.get_clock()

    @staticmethod
    async def _serialize_payload(user_id, room_id, account_data_type, content):
        payload = {
            "content": content,
        }

        return payload

    async def _handle_request(self, request, user_id, room_id, account_data_type):
        content = parse_json_object_from_request(request)

        max_stream_id = await self.handler.add_account_data_to_room(
            user_id, room_id, account_data_type, content["content"]
        )

        return 200, {"max_stream_id": max_stream_id}


class ReplicationAddTagRestServlet(ReplicationEndpoint):
    """Add tag on the appropriate account data worker.

    Request format:

        POST /_chat/replication/add_tag/:user_id/:room_id/:tag

        {
            "content": { ... },
        }

    """

    NAME = "add_tag"
    PATH_ARGS = ("user_id", "room_id", "tag")
    CACHE = False

    def __init__(self, hs):
        super().__init__(hs)

        self.handler = hs.get_account_data_handler()
        self.clock = hs.get_clock()

    @staticmethod
    async def _serialize_payload(user_id, room_id, tag, content):
        payload = {
            "content": content,
        }

        return payload

    async def _handle_request(self, request, user_id, room_id, tag):
        content = parse_json_object_from_request(request)

        max_stream_id = await self.handler.add_tag_to_room(
            user_id, room_id, tag, content["content"]
        )

        return 200, {"max_stream_id": max_stream_id}


class ReplicationRemoveTagRestServlet(ReplicationEndpoint):
    """Remove tag on the appropriate account data worker.

    Request format:

        POST /_chat/replication/remove_tag/:user_id/:room_id/:tag

        {}

    """

    NAME = "remove_tag"
    PATH_ARGS = (
        "user_id",
        "room_id",
        "tag",
    )
    CACHE = False

    def __init__(self, hs):
        super().__init__(hs)

        self.handler = hs.get_account_data_handler()
        self.clock = hs.get_clock()

    @staticmethod
    async def _serialize_payload(user_id, room_id, tag):

        return {}

    async def _handle_request(self, request, user_id, room_id, tag):
        max_stream_id = await self.handler.remove_tag_from_room(
            user_id,
            room_id,
            tag,
        )

        return 200, {"max_stream_id": max_stream_id}


def register_servlets(hs, http_server):
    ReplicationUserAccountDataRestServlet(hs).register(http_server)
    ReplicationRoomAccountDataRestServlet(hs).register(http_server)
    ReplicationAddTagRestServlet(hs).register(http_server)
    ReplicationRemoveTagRestServlet(hs).register(http_server)
