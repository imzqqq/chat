import logging
from typing import TYPE_CHECKING, List, Optional, Tuple

from chat.api.errors import SynapseError
from chat.handlers.room_member import RoomMemberHandler
from chat.replication.http.membership import (
    ReplicationRemoteJoinRestServlet as ReplRemoteJoin,
    ReplicationRemoteKnockRestServlet as ReplRemoteKnock,
    ReplicationRemoteRejectInviteRestServlet as ReplRejectInvite,
    ReplicationRemoteRescindKnockRestServlet as ReplRescindKnock,
    ReplicationUserJoinedLeftRoomRestServlet as ReplJoinedLeft,
)
from chat.types import JsonDict, Requester, UserID

if TYPE_CHECKING:
    from chat.server import HomeServer

logger = logging.getLogger(__name__)


class RoomMemberWorkerHandler(RoomMemberHandler):
    def __init__(self, hs: "HomeServer"):
        super().__init__(hs)

        self._remote_join_client = ReplRemoteJoin.make_client(hs)
        self._remote_knock_client = ReplRemoteKnock.make_client(hs)
        self._remote_reject_client = ReplRejectInvite.make_client(hs)
        self._remote_rescind_client = ReplRescindKnock.make_client(hs)
        self._notify_change_client = ReplJoinedLeft.make_client(hs)

    async def _remote_join(
        self,
        requester: Requester,
        remote_room_hosts: List[str],
        room_id: str,
        user: UserID,
        content: dict,
    ) -> Tuple[str, int]:
        """Implements RoomMemberHandler._remote_join"""
        if len(remote_room_hosts) == 0:
            raise SynapseError(404, "No known servers")

        ret = await self._remote_join_client(
            requester=requester,
            remote_room_hosts=remote_room_hosts,
            room_id=room_id,
            user_id=user.to_string(),
            content=content,
        )

        return ret["event_id"], ret["stream_id"]

    async def remote_reject_invite(
        self,
        invite_event_id: str,
        txn_id: Optional[str],
        requester: Requester,
        content: dict,
    ) -> Tuple[str, int]:
        """
        Rejects an out-of-band invite received from a remote user

        Implements RoomMemberHandler.remote_reject_invite
        """
        ret = await self._remote_reject_client(
            invite_event_id=invite_event_id,
            txn_id=txn_id,
            requester=requester,
            content=content,
        )
        return ret["event_id"], ret["stream_id"]

    async def remote_rescind_knock(
        self,
        knock_event_id: str,
        txn_id: Optional[str],
        requester: Requester,
        content: JsonDict,
    ) -> Tuple[str, int]:
        """
        Rescinds a local knock made on a remote room

        Args:
            knock_event_id: the knock event
            txn_id: optional transaction ID supplied by the client
            requester: user making the request, according to the access token
            content: additional content to include in the leave event.
               Normally an empty dict.

        Returns:
            A tuple containing (event_id, stream_id of the leave event)
        """
        ret = await self._remote_rescind_client(
            knock_event_id=knock_event_id,
            txn_id=txn_id,
            requester=requester,
            content=content,
        )
        return ret["event_id"], ret["stream_id"]

    async def remote_knock(
        self,
        remote_room_hosts: List[str],
        room_id: str,
        user: UserID,
        content: dict,
    ) -> Tuple[str, int]:
        """Sends a knock to a room.

        Implements RoomMemberHandler.remote_knock
        """
        ret = await self._remote_knock_client(
            remote_room_hosts=remote_room_hosts,
            room_id=room_id,
            user=user,
            content=content,
        )
        return ret["event_id"], ret["stream_id"]

    async def _user_left_room(self, target: UserID, room_id: str) -> None:
        """Implements RoomMemberHandler._user_left_room"""
        await self._notify_change_client(
            user_id=target.to_string(), room_id=room_id, change="left"
        )

    async def forget(self, target: UserID, room_id: str) -> None:
        raise RuntimeError("Cannot forget rooms on workers.")
