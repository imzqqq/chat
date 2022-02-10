"""Contains the URL paths to prefix various aspects of the server with. """
import hmac
from hashlib import sha256
from urllib.parse import urlencode

from chat.config import ConfigError

### MARK
SYNAPSE_CLIENT_API_PREFIX = "/_chat/client"
CLIENT_API_PREFIX = "/chat/client"
FEDERATION_PREFIX = "/chat/federation"
FEDERATION_V1_PREFIX = FEDERATION_PREFIX + "/v1"
FEDERATION_V2_PREFIX = FEDERATION_PREFIX + "/v2"
FEDERATION_UNSTABLE_PREFIX = FEDERATION_PREFIX + "/unstable"
STATIC_PREFIX = "/chat/static"
WEB_CLIENT_PREFIX = "/chat/client"
SERVER_KEY_V2_PREFIX = "/chat/key/v2"
MEDIA_PREFIX = "/chat/media/r0"
LEGACY_MEDIA_PREFIX = "/chat/media/v1"
###


class ConsentURIBuilder:
    def __init__(self, hs_config):
        """
        Args:
            hs_config (chat.config.homeserver.HomeServerConfig):
        """
        if hs_config.form_secret is None:
            raise ConfigError("form_secret not set in config")
        if hs_config.public_baseurl is None:
            raise ConfigError("public_baseurl not set in config")

        self._hmac_secret = hs_config.form_secret.encode("utf-8")
        self._public_baseurl = hs_config.public_baseurl

    def build_user_consent_uri(self, user_id):
        """Build a URI which we can give to the user to do their privacy
        policy consent

        Args:
            user_id (str): mxid or username of user

        Returns
            (str) the URI where the user can do consent
        """
        mac = hmac.new(
            key=self._hmac_secret, msg=user_id.encode("ascii"), digestmod=sha256
        ).hexdigest()
        ### MARK
        consent_uri = "%schat/consent?%s" % (
            self._public_baseurl,
            urlencode({"u": user_id, "h": mac}),
        )
        ###
        return consent_uri
