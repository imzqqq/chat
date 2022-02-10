import logging

from chat.http.servlet import RestServlet

from ._base import client_patterns

logger = logging.getLogger(__name__)


class PasswordPolicyServlet(RestServlet):
    PATTERNS = client_patterns("/password_policy$")

    def __init__(self, hs):
        """
        Args:
            hs (chat.server.HomeServer): server
        """
        super().__init__()

        self.policy = hs.config.password_policy
        self.enabled = hs.config.password_policy_enabled

    def on_GET(self, request):
        if not self.enabled or not self.policy:
            return (200, {})

        policy = {}

        for param in [
            "minimum_length",
            "require_digit",
            "require_symbol",
            "require_lowercase",
            "require_uppercase",
        ]:
            if param in self.policy:
                policy["m.%s" % param] = self.policy[param]

        return (200, policy)


def register_servlets(hs, http_server):
    PasswordPolicyServlet(hs).register(http_server)
