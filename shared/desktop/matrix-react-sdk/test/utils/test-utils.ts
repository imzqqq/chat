import { MatrixClient } from "matrix-js-sdk/src/client";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { EventType } from "matrix-js-sdk/src/@types/event";

import { AsyncStoreWithClient } from "../../src/stores/AsyncStoreWithClient";
import { mkEvent, mkStubRoom } from "../test-utils";
import { EnhancedMap } from "../../src/utils/maps";
import { EventEmitter } from "events";

// These methods make some use of some private methods on the AsyncStoreWithClient to simplify getting into a consistent
// ready state without needing to wire up a dispatcher and pretend to be a js-sdk client.

export const setupAsyncStoreWithClient = async (store: AsyncStoreWithClient<any>, client: MatrixClient) => {
    // @ts-ignore
    store.readyStore.useUnitTestClient(client);
    // @ts-ignore
    await store.onReady();
};

export const resetAsyncStoreWithClient = async (store: AsyncStoreWithClient<any>) => {
    // @ts-ignore
    await store.onNotReady();
};

export const mockStateEventImplementation = (events: MatrixEvent[]) => {
    const stateMap = new EnhancedMap<string, Map<string, MatrixEvent>>();
    events.forEach(event => {
        stateMap.getOrCreate(event.getType(), new Map()).set(event.getStateKey(), event);
    });

    return (eventType: string, stateKey?: string) => {
        if (stateKey || stateKey === "") {
            return stateMap.get(eventType)?.get(stateKey) || null;
        }
        return Array.from(stateMap.get(eventType)?.values() || []);
    };
};

export const mkRoom = (client: MatrixClient, roomId: string, rooms?: ReturnType<typeof mkStubRoom>[]) => {
    const room = mkStubRoom(roomId, roomId, client);
    room.currentState.getStateEvents.mockImplementation(mockStateEventImplementation([]));
    rooms?.push(room);
    return room;
};

export const mkSpace = (
    client: MatrixClient,
    spaceId: string,
    rooms?: ReturnType<typeof mkStubRoom>[],
    children: string[] = [],
) => {
    const space = mkRoom(client, spaceId, rooms);
    space.isSpaceRoom.mockReturnValue(true);
    space.currentState.getStateEvents.mockImplementation(mockStateEventImplementation(children.map(roomId =>
        mkEvent({
            event: true,
            type: EventType.SpaceChild,
            room: spaceId,
            user: "@user:server",
            skey: roomId,
            content: { via: [] },
            ts: Date.now(),
        }),
    )));
    return space;
};

export const emitPromise = (e: EventEmitter, k: string | symbol) => new Promise(r => e.once(k, r));
