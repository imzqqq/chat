import { useCallback, useState } from "react";
import { MatrixClient } from "matrix-js-sdk/src/client";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { Room } from "matrix-js-sdk/src/models/room";

import { useEventEmitter } from "./useEventEmitter";

const tryGetContent = <T extends {}>(ev?: MatrixEvent) => ev ? ev.getContent<T>() : undefined;

// Hook to simplify listening to Chat account data
export const useAccountData = <T extends {}>(cli: MatrixClient, eventType: string) => {
    const [value, setValue] = useState<T>(() => tryGetContent<T>(cli.getAccountData(eventType)));

    const handler = useCallback((event) => {
        if (event.getType() !== eventType) return;
        setValue(event.getContent());
    }, [eventType]);
    useEventEmitter(cli, "accountData", handler);

    return value || {} as T;
};

// Hook to simplify listening to Chat room account data
export const useRoomAccountData = <T extends {}>(room: Room, eventType: string) => {
    const [value, setValue] = useState<T>(() => tryGetContent<T>(room.getAccountData(eventType)));

    const handler = useCallback((event) => {
        if (event.getType() !== eventType) return;
        setValue(event.getContent());
    }, [eventType]);
    useEventEmitter(room, "Room.accountData", handler);

    return value || {} as T;
};
