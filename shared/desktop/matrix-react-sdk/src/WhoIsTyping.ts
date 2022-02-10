import { Room } from "matrix-js-sdk/src/models/room";
import { RoomMember } from "matrix-js-sdk/src/models/room-member";

import { MatrixClientPeg } from "./MatrixClientPeg";
import { _t } from './languageHandler';

export function usersTypingApartFromMeAndIgnored(room: Room): RoomMember[] {
    return usersTyping(room, [MatrixClientPeg.get().getUserId()].concat(MatrixClientPeg.get().getIgnoredUsers()));
}

export function usersTypingApartFromMe(room: Room): RoomMember[] {
    return usersTyping(room, [MatrixClientPeg.get().getUserId()]);
}

/**
 * Given a Room object and, optionally, a list of userID strings
 * to exclude, return a list of user objects who are typing.
 * @param {Room} room: room object to get users from.
 * @param {string[]} exclude: list of user mxids to exclude.
 * @returns {RoomMember[]} list of user objects who are typing.
 */
export function usersTyping(room: Room, exclude: string[] = []): RoomMember[] {
    const whoIsTyping = [];

    const memberKeys = Object.keys(room.currentState.members);
    for (let i = 0; i < memberKeys.length; ++i) {
        const userId = memberKeys[i];

        if (room.currentState.members[userId].typing) {
            if (exclude.indexOf(userId) === -1) {
                whoIsTyping.push(room.currentState.members[userId]);
            }
        }
    }

    return whoIsTyping;
}

export function whoIsTypingString(whoIsTyping: RoomMember[], limit: number): string {
    let othersCount = 0;
    if (whoIsTyping.length > limit) {
        othersCount = whoIsTyping.length - limit + 1;
    }

    if (whoIsTyping.length === 0) {
        return '';
    } else if (whoIsTyping.length === 1) {
        return _t('%(displayName)s is typing …', { displayName: whoIsTyping[0].name });
    }

    const names = whoIsTyping.map(m => m.name);

    if (othersCount >= 1) {
        return _t('%(names)s and %(count)s others are typing …', {
            names: names.slice(0, limit - 1).join(', '),
            count: othersCount,
        });
    } else {
        const lastPerson = names.pop();
        return _t('%(names)s and %(lastPerson)s are typing …', { names: names.join(', '), lastPerson: lastPerson });
    }
}
