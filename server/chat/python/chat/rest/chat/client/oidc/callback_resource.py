import logging
from typing import TYPE_CHECKING

from chat.http.server import DirectServeHtmlResource

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class OIDCCallbackResource(DirectServeHtmlResource):
    isLeaf = 1

    def __init__(self, hs: "HomeServer"):
        super().__init__()
        self._oidc_handler = hs.get_oidc_handler()

    async def _async_render_GET(self, request):
        await self._oidc_handler.handle_oidc_callback(request)

    async def _async_render_POST(self, request):
        # the auth response can be returned via an x-www-form-urlencoded form instead
        # of GET params, as per
        # https://openid.net/specs/oauth-v2-form-post-response-mode-1_0.html.
        await self._oidc_handler.handle_oidc_callback(request)
