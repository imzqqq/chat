import logging
from typing import Tuple

from chat.http import servlet
from chat.http.servlet import assert_params_in_dict, parse_json_object_from_request
from chat.logging.opentracing import set_tag, trace
from chat.rest.client.transactions import HttpTransactionCache

from ._base import client_patterns

logger = logging.getLogger(__name__)


class SendToDeviceRestServlet(servlet.RestServlet):
    PATTERNS = client_patterns(
        "/sendToDevice/(?P<message_type>[^/]*)/(?P<txn_id>[^/]*)$"
    )

    def __init__(self, hs):
        """
        Args:
            hs (chat.server.HomeServer): server
        """
        super().__init__()
        self.hs = hs
        self.auth = hs.get_auth()
        self.txns = HttpTransactionCache(hs)
        self.device_message_handler = hs.get_device_message_handler()

    @trace(opname="sendToDevice")
    def on_PUT(self, request, message_type, txn_id):
        set_tag("message_type", message_type)
        set_tag("txn_id", txn_id)
        return self.txns.fetch_or_execute_request(
            request, self._put, request, message_type, txn_id
        )

    async def _put(self, request, message_type, txn_id):
        requester = await self.auth.get_user_by_req(request, allow_guest=True)

        content = parse_json_object_from_request(request)
        assert_params_in_dict(content, ("messages",))

        await self.device_message_handler.send_device_message(
            requester, message_type, content["messages"]
        )

        response: Tuple[int, dict] = (200, {})
        return response


def register_servlets(hs, http_server):
    SendToDeviceRestServlet(hs).register(http_server)
