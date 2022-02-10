import logging

from twisted.web.resource import Resource

from chat.http.server import set_cors_headers
from chat.util import json_encoder

logger = logging.getLogger(__name__)


class WellKnownBuilder:
    """Utility to construct the well-known response

    Args:
        hs (chat.server.HomeServer):
    """

    def __init__(self, hs):
        self._config = hs.config

    def get_well_known(self):
        # if we don't have a public_baseurl, we can't help much here.
        if self._config.public_baseurl is None:
            return None

        result = {"m.homeserver": {"base_url": self._config.public_baseurl}}

        if self._config.default_identity_server:
            result["m.identity_server"] = {
                "base_url": self._config.default_identity_server
            }

        return result


class WellKnownResource(Resource):
    """A Twisted web resource which renders the .well-known file"""

    isLeaf = 1

    def __init__(self, hs):
        Resource.__init__(self)
        self._well_known_builder = WellKnownBuilder(hs)

    def render_GET(self, request):
        set_cors_headers(request)
        r = self._well_known_builder.get_well_known()
        if not r:
            request.setResponseCode(404)
            request.setHeader(b"Content-Type", b"text/plain")
            return b".well-known not available"

        logger.debug("returning: %s", r)
        request.setHeader(b"Content-Type", b"application/json")
        return json_encoder.encode(r).encode("utf-8")
