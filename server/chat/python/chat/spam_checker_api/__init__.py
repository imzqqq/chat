from enum import Enum


class RegistrationBehaviour(Enum):
    """
    Enum to define whether a registration request should allowed, denied, or shadow-banned.
    """

    ALLOW = "allow"
    SHADOW_BAN = "shadow_ban"
    DENY = "deny"
