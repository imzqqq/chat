import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { MatrixClientPeg } from "../../../MatrixClientPeg";
import { DefaultTagID, TagID } from "../models";

export function isSelf(event: MatrixEvent): boolean {
    const selfUserId = MatrixClientPeg.get().getUserId();
    if (event.getType() === 'm.room.member') {
        return event.getStateKey() === selfUserId;
    }
    return event.getSender() === selfUserId;
}

export function isSelfTarget(event: MatrixEvent): boolean {
    const selfUserId = MatrixClientPeg.get().getUserId();
    return event.getStateKey() === selfUserId;
}

export function shouldPrefixMessagesIn(roomId: string, tagId: TagID): boolean {
    if (tagId !== DefaultTagID.DM) return true;

    // We don't prefix anything in 1:1s
    const room = MatrixClientPeg.get().getRoom(roomId);
    if (!room) return true;
    return room.currentState.getJoinedMemberCount() !== 2;
}

export function getSenderName(event: MatrixEvent): string {
    return event.sender ? event.sender.name : event.getSender();
}

export function getTargetName(event: MatrixEvent): string {
    return event.target ? event.target.name : event.getStateKey();
}
