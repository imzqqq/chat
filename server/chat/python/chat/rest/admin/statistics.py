import logging
from typing import TYPE_CHECKING, Tuple

from chat.api.errors import Codes, SynapseError
from chat.http.servlet import RestServlet, parse_integer, parse_string
from chat.http.site import SynapseRequest
from chat.rest.admin._base import admin_patterns, assert_requester_is_admin
from chat.storage.databases.main.stats import UserSortOrder
from chat.types import JsonDict

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class UserMediaStatisticsRestServlet(RestServlet):
    """
    Get statistics about uploaded media by users.
    """

    PATTERNS = admin_patterns("/statistics/users/media$")

    def __init__(self, hs: "HomeServer"):
        self.hs = hs
        self.auth = hs.get_auth()
        self.store = hs.get_datastore()

    async def on_GET(self, request: SynapseRequest) -> Tuple[int, JsonDict]:
        await assert_requester_is_admin(self.auth, request)

        order_by = parse_string(
            request, "order_by", default=UserSortOrder.USER_ID.value
        )
        if order_by not in (
            UserSortOrder.MEDIA_LENGTH.value,
            UserSortOrder.MEDIA_COUNT.value,
            UserSortOrder.USER_ID.value,
            UserSortOrder.DISPLAYNAME.value,
        ):
            raise SynapseError(
                400,
                "Unknown value for order_by: %s" % (order_by,),
                errcode=Codes.INVALID_PARAM,
            )

        start = parse_integer(request, "from", default=0)
        if start < 0:
            raise SynapseError(
                400,
                "Query parameter from must be a string representing a positive integer.",
                errcode=Codes.INVALID_PARAM,
            )

        limit = parse_integer(request, "limit", default=100)
        if limit < 0:
            raise SynapseError(
                400,
                "Query parameter limit must be a string representing a positive integer.",
                errcode=Codes.INVALID_PARAM,
            )

        from_ts = parse_integer(request, "from_ts", default=0)
        if from_ts < 0:
            raise SynapseError(
                400,
                "Query parameter from_ts must be a string representing a positive integer.",
                errcode=Codes.INVALID_PARAM,
            )

        until_ts = parse_integer(request, "until_ts")
        if until_ts is not None:
            if until_ts < 0:
                raise SynapseError(
                    400,
                    "Query parameter until_ts must be a string representing a positive integer.",
                    errcode=Codes.INVALID_PARAM,
                )
            if until_ts <= from_ts:
                raise SynapseError(
                    400,
                    "Query parameter until_ts must be greater than from_ts.",
                    errcode=Codes.INVALID_PARAM,
                )

        search_term = parse_string(request, "search_term")
        if search_term == "":
            raise SynapseError(
                400,
                "Query parameter search_term cannot be an empty string.",
                errcode=Codes.INVALID_PARAM,
            )

        direction = parse_string(request, "dir", default="f")
        if direction not in ("f", "b"):
            raise SynapseError(
                400, "Unknown direction: %s" % (direction,), errcode=Codes.INVALID_PARAM
            )

        users_media, total = await self.store.get_users_media_usage_paginate(
            start, limit, from_ts, until_ts, order_by, direction, search_term
        )
        ret = {"users": users_media, "total": total}
        if (start + limit) < total:
            ret["next_token"] = start + len(users_media)

        return 200, ret
