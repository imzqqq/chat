import { useCallback, useEffect, useState } from "react";
import { Room } from "matrix-js-sdk/src/models/room";
import { RoomState } from "matrix-js-sdk/src/models/room-state";

import { useEventEmitter } from "./useEventEmitter";

type Mapper<T> = (roomState: RoomState) => T;
const defaultMapper: Mapper<RoomState> = (roomState: RoomState) => roomState;

// Hook to simplify watching Chat Room state
export const useRoomState = <T extends any = RoomState>(
    room?: Room,
    mapper: Mapper<T> = defaultMapper as Mapper<T>,
): T => {
    const [value, setValue] = useState<T>(room ? mapper(room.currentState) : undefined);

    const update = useCallback(() => {
        if (!room) return;
        setValue(mapper(room.currentState));
    }, [room, mapper]);

    useEventEmitter(room?.currentState, "RoomState.events", update);
    useEffect(() => {
        update();
        return () => {
            setValue(undefined);
        };
    }, [update]);
    return value;
};
