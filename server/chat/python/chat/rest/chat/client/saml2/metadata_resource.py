import saml2.metadata

from twisted.web.resource import Resource


class SAML2MetadataResource(Resource):
    """A Twisted web resource which renders the SAML metadata"""

    isLeaf = 1

    def __init__(self, hs):
        Resource.__init__(self)
        self.sp_config = hs.config.saml2_sp_config

    def render_GET(self, request):
        metadata_xml = saml2.metadata.create_metadata_string(
            configfile=None, config=self.sp_config
        )
        request.setHeader(b"Content-Type", b"text/xml; charset=utf-8")
        return metadata_xml
