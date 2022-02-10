import logging
from typing import List, Optional, Tuple

import attr

from chat.types import PersistedEventPosition

logger = logging.getLogger(__name__)


@attr.s(slots=True, frozen=True, weakref_slot=False, auto_attribs=True)
class RoomsForUser:
    room_id: str
    sender: str
    membership: str
    event_id: str
    stream_ordering: int
    room_version_id: str


@attr.s(slots=True, frozen=True, weakref_slot=False, auto_attribs=True)
class GetRoomsForUserWithStreamOrdering:
    room_id: str
    event_pos: PersistedEventPosition


@attr.s(slots=True, frozen=True, weakref_slot=False, auto_attribs=True)
class ProfileInfo:
    avatar_url: Optional[str]
    display_name: Optional[str]


@attr.s(slots=True, frozen=True, weakref_slot=False, auto_attribs=True)
class MemberSummary:
    # A truncated list of (user_id, event_id) tuples for users of a given
    # membership type, suitable for use in calculating heroes for a room.
    members: List[Tuple[str, str]]
    # The total number of users of a given membership type.
    count: int
