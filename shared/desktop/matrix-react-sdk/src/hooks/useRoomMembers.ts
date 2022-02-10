import { useState } from "react";
import { Room } from "matrix-js-sdk/src/models/room";
import { RoomMember } from "matrix-js-sdk/src/models/room-member";

import { useEventEmitter } from "./useEventEmitter";
import { throttle } from "lodash";

// Hook to simplify watching Chat Room joined members
export const useRoomMembers = (room: Room, throttleWait = 250) => {
    const [members, setMembers] = useState<RoomMember[]>(room.getJoinedMembers());
    useEventEmitter(room.currentState, "RoomState.members", throttle(() => {
        setMembers(room.getJoinedMembers());
    }, throttleWait, { leading: true, trailing: true }));
    return members;
};

// Hook to simplify watching Chat Room joined member count
export const useRoomMemberCount = (room: Room, throttleWait = 250) => {
    const [count, setCount] = useState<number>(room.getJoinedMemberCount());
    useEventEmitter(room.currentState, "RoomState.members", throttle(() => {
        setCount(room.getJoinedMemberCount());
    }, throttleWait, { leading: true, trailing: true }));
    return count;
};
