import logging

import attr
from signedjson.types import VerifyKey

logger = logging.getLogger(__name__)


@attr.s(slots=True, frozen=True)
class FetchKeyResult:
    verify_key = attr.ib(type=VerifyKey)  # the key itself
    valid_until_ts = attr.ib(type=int)  # how long we can use this key for
