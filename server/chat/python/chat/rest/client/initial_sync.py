from chat.http.servlet import RestServlet, parse_boolean
from chat.rest.client._base import client_patterns
from chat.streams.config import PaginationConfig


# TODO: Needs unit testing
class InitialSyncRestServlet(RestServlet):
    PATTERNS = client_patterns("/initialSync$", v1=True)

    def __init__(self, hs):
        super().__init__()
        self.initial_sync_handler = hs.get_initial_sync_handler()
        self.auth = hs.get_auth()
        self.store = hs.get_datastore()

    async def on_GET(self, request):
        requester = await self.auth.get_user_by_req(request)
        as_client_event = b"raw" not in request.args
        pagination_config = await PaginationConfig.from_request(self.store, request)
        include_archived = parse_boolean(request, "archived", default=False)
        content = await self.initial_sync_handler.snapshot_all_rooms(
            user_id=requester.user.to_string(),
            pagin_config=pagination_config,
            as_client_event=as_client_event,
            include_archived=include_archived,
        )

        return 200, content


def register_servlets(hs, http_server):
    InitialSyncRestServlet(hs).register(http_server)
