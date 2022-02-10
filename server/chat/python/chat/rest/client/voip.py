import base64
import hashlib
import hmac

from chat.http.servlet import RestServlet
from chat.rest.client._base import client_patterns


class VoipRestServlet(RestServlet):
    PATTERNS = client_patterns("/voip/turnServer$", v1=True)

    def __init__(self, hs):
        super().__init__()
        self.hs = hs
        self.auth = hs.get_auth()

    async def on_GET(self, request):
        requester = await self.auth.get_user_by_req(
            request, self.hs.config.turn_allow_guests
        )

        turnUris = self.hs.config.turn_uris
        turnSecret = self.hs.config.turn_shared_secret
        turnUsername = self.hs.config.turn_username
        turnPassword = self.hs.config.turn_password
        userLifetime = self.hs.config.turn_user_lifetime

        if turnUris and turnSecret and userLifetime:
            expiry = (self.hs.get_clock().time_msec() + userLifetime) / 1000
            username = "%d:%s" % (expiry, requester.user.to_string())

            mac = hmac.new(
                turnSecret.encode(), msg=username.encode(), digestmod=hashlib.sha1
            )
            # We need to use standard padded base64 encoding here
            # encode_base64 because we need to add the standard padding to get the
            # same result as the TURN server.
            password = base64.b64encode(mac.digest()).decode("ascii")

        elif turnUris and turnUsername and turnPassword and userLifetime:
            username = turnUsername
            password = turnPassword

        else:
            return 200, {}

        return (
            200,
            {
                "username": username,
                "password": password,
                "ttl": userLifetime / 1000,
                "uris": turnUris,
            },
        )


def register_servlets(hs, http_server):
    VoipRestServlet(hs).register(http_server)
