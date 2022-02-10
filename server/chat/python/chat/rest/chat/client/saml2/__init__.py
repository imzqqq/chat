import logging

from twisted.web.resource import Resource

from chat.rest.chat.client.saml2.metadata_resource import SAML2MetadataResource
from chat.rest.chat.client.saml2.response_resource import SAML2ResponseResource

logger = logging.getLogger(__name__)


class SAML2Resource(Resource):
    def __init__(self, hs):
        Resource.__init__(self)
        self.putChild(b"metadata.xml", SAML2MetadataResource(hs))
        self.putChild(b"authn_response", SAML2ResponseResource(hs))


__all__ = ["SAML2Resource"]
