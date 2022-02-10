import re
from typing import Iterable, Pattern

from chat.api.auth import Auth
from chat.api.errors import AuthError
from chat.http.site import SynapseRequest
from chat.types import UserID


def admin_patterns(path_regex: str, version: str = "v1") -> Iterable[Pattern]:
    """Returns the list of patterns for an admin endpoint

    Args:
        path_regex: The regex string to match. This should NOT have a ^
            as this will be prefixed.

    Returns:
        A list of regex patterns.
    """
    ### MARK
    admin_prefix = "^/_chat/admin/" + version
    ###
    patterns = [re.compile(admin_prefix + path_regex)]
    return patterns


async def assert_requester_is_admin(auth: Auth, request: SynapseRequest) -> None:
    """Verify that the requester is an admin user

    Args:
        auth: Auth singleton
        request: incoming request

    Raises:
        AuthError if the requester is not a server admin
    """
    requester = await auth.get_user_by_req(request)
    await assert_user_is_admin(auth, requester.user)


async def assert_user_is_admin(auth: Auth, user_id: UserID) -> None:
    """Verify that the given user is an admin user

    Args:
        auth: Auth singleton
        user_id: user to check

    Raises:
        AuthError if the user is not a server admin
    """
    is_admin = await auth.is_server_admin(user_id)
    if not is_admin:
        raise AuthError(403, "You are not a server admin")
