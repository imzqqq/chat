import { Room } from "matrix-js-sdk/src/models/room";
import { FILTER_CHANGED, FilterKind, IFilterCondition } from "./IFilterCondition";
import { Group } from "matrix-js-sdk/src/models/group";
import { EventEmitter } from "events";
import GroupStore from "../../GroupStore";
import { IDestroyable } from "../../../utils/IDestroyable";
import DMRoomMap from "../../../utils/DMRoomMap";
import { setHasDiff } from "../../../utils/sets";

/**
 * A filter condition for the room list which reveals rooms which
 * are a member of a given community.
 */
export class CommunityFilterCondition extends EventEmitter implements IFilterCondition, IDestroyable {
    private roomIds = new Set<string>();
    private userIds = new Set<string>();

    constructor(private community: Group) {
        super();
        GroupStore.on("update", this.onStoreUpdate);

        // noinspection JSIgnoredPromiseFromCall
        this.onStoreUpdate(); // trigger a false update to seed the store
    }

    public get kind(): FilterKind {
        return FilterKind.Prefilter;
    }

    public isVisible(room: Room): boolean {
        return this.roomIds.has(room.roomId) || this.userIds.has(DMRoomMap.shared().getUserIdForRoomId(room.roomId));
    }

    private onStoreUpdate = async (): Promise<any> => {
        // We don't actually know if the room list changed for the community, so just check it again.
        const beforeRoomIds = this.roomIds;
        this.roomIds = new Set((await GroupStore.getGroupRooms(this.community.groupId)).map(r => r.roomId));

        const beforeUserIds = this.userIds;
        this.userIds = new Set((await GroupStore.getGroupMembers(this.community.groupId)).map(u => u.userId));

        if (setHasDiff(beforeRoomIds, this.roomIds) || setHasDiff(beforeUserIds, this.userIds)) {
            this.emit(FILTER_CHANGED);
        }
    };

    public destroy(): void {
        GroupStore.off("update", this.onStoreUpdate);
    }
}
