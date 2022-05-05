import { useCallback, useState } from "react";
import { MatrixClient } from "matrix-js-sdk/src/client";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { Room } from "matrix-js-sdk/src/models/room";

import { useEventEmitter } from "./useEventEmitter";

// Hook to simplify watching whether a Chat room is encrypted, returns undefined if room is undefined
export function useIsEncrypted(cli: MatrixClient, room?: Room): boolean | undefined {
    const [isEncrypted, setIsEncrypted] = useState(room ? cli.isRoomEncrypted(room.roomId) : undefined);

    const update = useCallback((event: MatrixEvent) => {
        if (room && event.getType() === "m.room.encryption") {
            setIsEncrypted(cli.isRoomEncrypted(room.roomId));
        }
    }, [cli, room]);
    useEventEmitter(room ? room.currentState : undefined, "RoomState.events", update);

    return isEncrypted;
}
