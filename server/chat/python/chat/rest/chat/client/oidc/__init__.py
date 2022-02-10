import logging

from twisted.web.resource import Resource

from chat.rest.chat.client.oidc.callback_resource import OIDCCallbackResource

logger = logging.getLogger(__name__)


class OIDCResource(Resource):
    def __init__(self, hs):
        Resource.__init__(self)
        self.putChild(b"callback", OIDCCallbackResource(hs))


__all__ = ["OIDCResource"]
