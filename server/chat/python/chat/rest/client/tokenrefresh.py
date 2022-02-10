from chat.api.errors import AuthError
from chat.http.servlet import RestServlet

from ._base import client_patterns


class TokenRefreshRestServlet(RestServlet):
    """
    Exchanges refresh tokens for a pair of an access token and a new refresh
    token.
    """

    PATTERNS = client_patterns("/tokenrefresh")

    def __init__(self, hs):
        super().__init__()

    async def on_POST(self, request):
        raise AuthError(403, "tokenrefresh is no longer supported.")


def register_servlets(hs, http_server):
    TokenRefreshRestServlet(hs).register(http_server)
