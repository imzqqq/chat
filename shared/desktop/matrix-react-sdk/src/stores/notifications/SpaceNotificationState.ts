import { Room } from "matrix-js-sdk/src/models/room";

import { NotificationColor } from "./NotificationColor";
import { arrayDiff } from "../../utils/arrays";
import { RoomNotificationState } from "./RoomNotificationState";
import { NOTIFICATION_STATE_UPDATE, NotificationState } from "./NotificationState";
import { FetchRoomFn } from "./ListNotificationState";

export class SpaceNotificationState extends NotificationState {
    public rooms: Room[] = []; // exposed only for tests
    private states: { [spaceId: string]: RoomNotificationState } = {};

    constructor(private spaceId: string | symbol, private getRoomFn: FetchRoomFn) {
        super();
    }

    public get symbol(): string {
        return this._color === NotificationColor.Unsent ? "!" : null;
    }

    public setRooms(rooms: Room[]) {
        const oldRooms = this.rooms;
        const diff = arrayDiff(oldRooms, rooms);
        this.rooms = rooms;
        for (const oldRoom of diff.removed) {
            const state = this.states[oldRoom.roomId];
            if (!state) continue; // We likely just didn't have a badge (race condition)
            delete this.states[oldRoom.roomId];
            state.off(NOTIFICATION_STATE_UPDATE, this.onRoomNotificationStateUpdate);
        }
        for (const newRoom of diff.added) {
            const state = this.getRoomFn(newRoom);
            state.on(NOTIFICATION_STATE_UPDATE, this.onRoomNotificationStateUpdate);
            this.states[newRoom.roomId] = state;
        }

        this.calculateTotalState();
    }

    public getFirstRoomWithNotifications() {
        return Object.values(this.states).find(state => state.color >= this.color)?.room.roomId;
    }

    public destroy() {
        super.destroy();
        for (const state of Object.values(this.states)) {
            state.off(NOTIFICATION_STATE_UPDATE, this.onRoomNotificationStateUpdate);
        }
        this.states = {};
    }

    private onRoomNotificationStateUpdate = () => {
        this.calculateTotalState();
    };

    private calculateTotalState() {
        const snapshot = this.snapshot();

        this._count = 0;
        this._color = NotificationColor.None;
        for (const state of Object.values(this.states)) {
            this._count += state.count;
            this._color = Math.max(this.color, state.color);
        }

        // finally, publish an update if needed
        this.emitIfUpdated(snapshot);
    }
}
