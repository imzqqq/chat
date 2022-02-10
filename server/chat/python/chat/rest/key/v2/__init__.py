from twisted.web.resource import Resource

from .local_key_resource import LocalKey
from .remote_key_resource import RemoteKey


class KeyApiV2Resource(Resource):
    def __init__(self, hs):
        Resource.__init__(self)
        self.putChild(b"server", LocalKey(hs))
        self.putChild(b"query", RemoteKey(hs))
