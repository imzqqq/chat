"""Exception types which are exposed as part of the stable module API"""

from chat.api.errors import (  # noqa: F401
    InvalidClientCredentialsError,
    RedirectException,
    SynapseError,
)
from chat.config._base import ConfigError  # noqa: F401
