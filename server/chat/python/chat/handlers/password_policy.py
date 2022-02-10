import logging
import re
from typing import TYPE_CHECKING

from chat.api.errors import Codes, PasswordRefusedError

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class PasswordPolicyHandler:
    def __init__(self, hs: "HomeServer"):
        self.policy = hs.config.password_policy
        self.enabled = hs.config.password_policy_enabled

        # Regexps for the spec'd policy parameters.
        self.regexp_digit = re.compile("[0-9]")
        self.regexp_symbol = re.compile("[^a-zA-Z0-9]")
        self.regexp_uppercase = re.compile("[A-Z]")
        self.regexp_lowercase = re.compile("[a-z]")

    def validate_password(self, password: str) -> None:
        """Checks whether a given password complies with the server's policy.

        Args:
            password: The password to check against the server's policy.

        Raises:
            PasswordRefusedError: The password doesn't comply with the server's policy.
        """

        if not self.enabled:
            return

        minimum_accepted_length = self.policy.get("minimum_length", 0)
        if len(password) < minimum_accepted_length:
            raise PasswordRefusedError(
                msg=(
                    "The password must be at least %d characters long"
                    % minimum_accepted_length
                ),
                errcode=Codes.PASSWORD_TOO_SHORT,
            )

        if (
            self.policy.get("require_digit", False)
            and self.regexp_digit.search(password) is None
        ):
            raise PasswordRefusedError(
                msg="The password must include at least one digit",
                errcode=Codes.PASSWORD_NO_DIGIT,
            )

        if (
            self.policy.get("require_symbol", False)
            and self.regexp_symbol.search(password) is None
        ):
            raise PasswordRefusedError(
                msg="The password must include at least one symbol",
                errcode=Codes.PASSWORD_NO_SYMBOL,
            )

        if (
            self.policy.get("require_uppercase", False)
            and self.regexp_uppercase.search(password) is None
        ):
            raise PasswordRefusedError(
                msg="The password must include at least one uppercase letter",
                errcode=Codes.PASSWORD_NO_UPPERCASE,
            )

        if (
            self.policy.get("require_lowercase", False)
            and self.regexp_lowercase.search(password) is None
        ):
            raise PasswordRefusedError(
                msg="The password must include at least one lowercase letter",
                errcode=Codes.PASSWORD_NO_LOWERCASE,
            )
