"""This module implements user-interactive auth verification.

TODO: move more stuff out of AuthHandler in here.

"""

from chat.handlers.ui_auth.checkers import INTERACTIVE_AUTH_CHECKERS  # noqa: F401


class UIAuthSessionDataConstants:
    """Constants for use with AuthHandler.set_session_data"""

    # used during registration and password reset to store a hashed copy of the
    # password, so that the client does not need to submit it each time.
    PASSWORD_HASH = "password_hash"

    # used during registration to store the mxid of the registered user
    REGISTERED_USER_ID = "registered_user_id"

    # used by validate_user_via_ui_auth to store the mxid of the user we are validating
    # for.
    REQUEST_USER_ID = "request_user_id"
