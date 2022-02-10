import logging
from typing import Optional

import attr

from chat.api.errors import SynapseError
from chat.http.servlet import parse_integer, parse_string
from chat.http.site import SynapseRequest
from chat.storage.databases.main import DataStore
from chat.types import StreamToken

logger = logging.getLogger(__name__)


MAX_LIMIT = 1000


@attr.s(slots=True)
class PaginationConfig:
    """A configuration object which stores pagination parameters."""

    from_token = attr.ib(type=Optional[StreamToken])
    to_token = attr.ib(type=Optional[StreamToken])
    direction = attr.ib(type=str)
    limit = attr.ib(type=Optional[int])

    @classmethod
    async def from_request(
        cls,
        store: "DataStore",
        request: SynapseRequest,
        raise_invalid_params: bool = True,
        default_limit: Optional[int] = None,
    ) -> "PaginationConfig":
        direction = parse_string(request, "dir", default="f", allowed_values=["f", "b"])

        from_tok_str = parse_string(request, "from")
        to_tok_str = parse_string(request, "to")

        try:
            from_tok = None
            if from_tok_str == "END":
                from_tok = None  # For backwards compat.
            elif from_tok_str:
                from_tok = await StreamToken.from_string(store, from_tok_str)
        except Exception:
            raise SynapseError(400, "'from' parameter is invalid")

        try:
            to_tok = None
            if to_tok_str:
                to_tok = await StreamToken.from_string(store, to_tok_str)
        except Exception:
            raise SynapseError(400, "'to' parameter is invalid")

        limit = parse_integer(request, "limit", default=default_limit)

        if limit:
            if limit < 0:
                raise SynapseError(400, "Limit must be 0 or above")

            limit = min(int(limit), MAX_LIMIT)

        try:
            return PaginationConfig(from_tok, to_tok, direction, limit)
        except Exception:
            logger.exception("Failed to create pagination config")
            raise SynapseError(400, "Invalid request.")

    def __repr__(self) -> str:
        return ("PaginationConfig(from_tok=%r, to_tok=%r, direction=%r, limit=%r)") % (
            self.from_token,
            self.to_token,
            self.direction,
            self.limit,
        )
