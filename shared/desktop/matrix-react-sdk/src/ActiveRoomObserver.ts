import { EventSubscription } from 'fbemitter';
import RoomViewStore from './stores/RoomViewStore';

type Listener = (isActive: boolean) => void;

/**
 * Consumes changes from the RoomViewStore and notifies specific things
 * about when the active room changes. Unlike listening for RoomViewStore
 * changes, you can subscribe to only changes relevant to a particular
 * room.
 *
 * TODO: If we introduce an observer for something else, factor out
 * the adding / removing of listeners & emitting into a common class.
 */
export class ActiveRoomObserver {
    private listeners: {[key: string]: Listener[]} = {};
    private _activeRoomId = RoomViewStore.getRoomId();
    private readonly roomStoreToken: EventSubscription;

    constructor() {
        // TODO: We could self-destruct when the last listener goes away, or at least stop listening.
        this.roomStoreToken = RoomViewStore.addListener(this.onRoomViewStoreUpdate);
    }

    public get activeRoomId(): string {
        return this._activeRoomId;
    }

    public addListener(roomId, listener) {
        if (!this.listeners[roomId]) this.listeners[roomId] = [];
        this.listeners[roomId].push(listener);
    }

    public removeListener(roomId, listener) {
        if (this.listeners[roomId]) {
            const i = this.listeners[roomId].indexOf(listener);
            if (i > -1) {
                this.listeners[roomId].splice(i, 1);
            }
        } else {
            console.warn("Unregistering unrecognised listener (roomId=" + roomId + ")");
        }
    }

    private emit(roomId, isActive: boolean) {
        if (!this.listeners[roomId]) return;

        for (const l of this.listeners[roomId]) {
            l.call(null, isActive);
        }
    }

    private onRoomViewStoreUpdate = () => {
        // emit for the old room ID
        if (this._activeRoomId) this.emit(this._activeRoomId, false);

        // update our cache
        this._activeRoomId = RoomViewStore.getRoomId();

        // and emit for the new one
        if (this._activeRoomId) this.emit(this._activeRoomId, true);
    };
}

if (window.mxActiveRoomObserver === undefined) {
    window.mxActiveRoomObserver = new ActiveRoomObserver();
}
export default window.mxActiveRoomObserver;
