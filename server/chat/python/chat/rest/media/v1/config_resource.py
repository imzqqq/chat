from typing import TYPE_CHECKING

from twisted.web.server import Request

from chat.http.server import DirectServeJsonResource, respond_with_json
from chat.http.site import SynapseRequest

if TYPE_CHECKING:
    from chat.server import HomeServer


class MediaConfigResource(DirectServeJsonResource):
    isLeaf = True

    def __init__(self, hs: "HomeServer"):
        super().__init__()
        config = hs.config
        self.clock = hs.get_clock()
        self.auth = hs.get_auth()
        self.limits_dict = {"m.upload.size": config.max_upload_size}

    async def _async_render_GET(self, request: SynapseRequest) -> None:
        await self.auth.get_user_by_req(request)
        respond_with_json(request, 200, self.limits_dict, send_cors=True)

    async def _async_render_OPTIONS(self, request: Request) -> None:
        respond_with_json(request, 200, {}, send_cors=True)
