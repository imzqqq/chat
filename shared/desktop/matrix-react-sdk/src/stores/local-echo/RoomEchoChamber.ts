import { GenericEchoChamber, implicitlyReverted, PROPERTY_UPDATED } from "./GenericEchoChamber";
import { getRoomNotifsState, setRoomNotifsState } from "../../RoomNotifs";
import { RoomEchoContext } from "./RoomEchoContext";
import { _t } from "../../languageHandler";
import { Volume } from "../../RoomNotifsTypes";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";

export type CachedRoomValues = Volume;

export enum CachedRoomKey {
    NotificationVolume,
}

export class RoomEchoChamber extends GenericEchoChamber<RoomEchoContext, CachedRoomKey, CachedRoomValues> {
    private properties = new Map<CachedRoomKey, CachedRoomValues>();

    public constructor(context: RoomEchoContext) {
        super(context, (k) => this.properties.get(k));
    }

    protected onClientChanged(oldClient, newClient) {
        this.properties.clear();
        if (oldClient) {
            oldClient.removeListener("accountData", this.onAccountData);
        }
        if (newClient) {
            // Register the listeners first
            newClient.on("accountData", this.onAccountData);

            // Then populate the properties map
            this.updateNotificationVolume();
        }
    }

    private onAccountData = (event: MatrixEvent) => {
        if (event.getType() === "m.push_rules") {
            const currentVolume = this.properties.get(CachedRoomKey.NotificationVolume) as Volume;
            const newVolume = getRoomNotifsState(this.context.room.roomId) as Volume;
            if (currentVolume !== newVolume) {
                this.updateNotificationVolume();
            }
        }
    };

    private updateNotificationVolume() {
        this.properties.set(CachedRoomKey.NotificationVolume, getRoomNotifsState(this.context.room.roomId));
        this.markEchoReceived(CachedRoomKey.NotificationVolume);
        this.emit(PROPERTY_UPDATED, CachedRoomKey.NotificationVolume);
    }

    // ---- helpers below here ----

    public get notificationVolume(): Volume {
        return this.getValue(CachedRoomKey.NotificationVolume);
    }

    public set notificationVolume(v: Volume) {
        this.setValue(_t("Change notification settings"), CachedRoomKey.NotificationVolume, v, async () => {
            return setRoomNotifsState(this.context.room.roomId, v);
        }, implicitlyReverted);
    }
}
