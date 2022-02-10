from typing import TYPE_CHECKING

from chat.http.server import DirectServeHtmlResource

if TYPE_CHECKING:
    from chat.server import HomeServer


class SAML2ResponseResource(DirectServeHtmlResource):
    """A Twisted web resource which handles the SAML response"""

    isLeaf = 1

    def __init__(self, hs: "HomeServer"):
        super().__init__()
        self._saml_handler = hs.get_saml_handler()
        self._sso_handler = hs.get_sso_handler()

    async def _async_render_GET(self, request):
        # We're not expecting any GET request on that resource if everything goes right,
        # but some IdPs sometimes end up responding with a 302 redirect on this endpoint.
        # In this case, just tell the user that something went wrong and they should
        # try to authenticate again.
        self._sso_handler.render_error(
            request, "unexpected_get", "Unexpected GET request on /saml2/authn_response"
        )

    async def _async_render_POST(self, request):
        await self._saml_handler.handle_saml_response(request)
