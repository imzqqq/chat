from twisted.web.resource import Resource


class HealthResource(Resource):
    """A resource that does nothing except return a 200 with a body of `OK`,
    which can be used as a health check.

    Note: `SynapseRequest._should_log_request` ensures that requests to
    `/health` do not get logged at INFO.
    """

    isLeaf = 1

    def render_GET(self, request):
        request.setHeader(b"Content-Type", b"text/plain")
        return b"OK"
