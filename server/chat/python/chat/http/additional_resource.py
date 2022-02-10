from typing import TYPE_CHECKING

from twisted.web.server import Request

from chat.http.server import DirectServeJsonResource

if TYPE_CHECKING:
    from chat.server import HomeServer


class AdditionalResource(DirectServeJsonResource):
    """Resource wrapper for additional_resources

    If the user has configured additional_resources, we need to wrap the
    handler class with a Resource so that we can map it into the resource tree.

    This class is also where we wrap the request handler with logging, metrics,
    and exception handling.
    """

    def __init__(self, hs: "HomeServer", handler):
        """Initialise AdditionalResource

        The ``handler`` should return a deferred which completes when it has
        done handling the request. It should write a response with
        ``request.write()``, and call ``request.finish()``.

        Args:
            hs: homeserver
            handler ((twisted.web.server.Request) -> twisted.internet.defer.Deferred):
                function to be called to handle the request.
        """
        super().__init__()
        self._handler = handler

    def _async_render(self, request: Request):
        # Cheekily pass the result straight through, so we don't need to worry
        # if its an awaitable or not.
        return self._handler(request)
