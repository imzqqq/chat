import { Room } from "matrix-js-sdk/src/models/room";
import CallHandler from "../../../CallHandler";
import { RoomListCustomisations } from "../../../customisations/RoomList";
import VoipUserMapper from "../../../VoipUserMapper";
import SpaceStore from "../../spaces/SpaceStore";

export class VisibilityProvider {
    private static internalInstance: VisibilityProvider;

    private constructor() {
    }

    public static get instance(): VisibilityProvider {
        if (!VisibilityProvider.internalInstance) {
            VisibilityProvider.internalInstance = new VisibilityProvider();
        }
        return VisibilityProvider.internalInstance;
    }

    public async onNewInvitedRoom(room: Room) {
        await VoipUserMapper.sharedInstance().onNewInvitedRoom(room);
    }

    public isRoomVisible(room?: Room): boolean {
        if (!room) {
            return false;
        }

        if (
            CallHandler.sharedInstance().getSupportsVirtualRooms() &&
            VoipUserMapper.sharedInstance().isVirtualRoom(room)
        ) {
            return false;
        }

        // hide space rooms as they'll be shown in the SpacePanel
        if (SpaceStore.spacesEnabled && room.isSpaceRoom()) {
            return false;
        }

        const isVisibleFn = RoomListCustomisations.isRoomVisible;
        if (isVisibleFn) {
            return isVisibleFn(room);
        }

        return true; // default
    }
}
