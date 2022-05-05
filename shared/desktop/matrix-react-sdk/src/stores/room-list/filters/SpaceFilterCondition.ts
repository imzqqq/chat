import { EventEmitter } from "events";
import { Room } from "matrix-js-sdk/src/models/room";

import { FILTER_CHANGED, FilterKind, IFilterCondition } from "./IFilterCondition";
import { IDestroyable } from "../../../utils/IDestroyable";
import SpaceStore from "../../spaces/SpaceStore";
import { MetaSpace, SpaceKey } from "../../spaces";
import { setHasDiff } from "../../../utils/sets";

/**
 * A filter condition for the room list which reveals rooms which
 * are a member of a given space or if no space is selected shows:
 *  + Orphaned rooms (ones not in any space you are a part of)
 *  + All DMs
 */
export class SpaceFilterCondition extends EventEmitter implements IFilterCondition, IDestroyable {
    private roomIds = new Set<string>();
    private space: SpaceKey = MetaSpace.Home;

    public get kind(): FilterKind {
        return FilterKind.Prefilter;
    }

    public isVisible(room: Room): boolean {
        return this.roomIds.has(room.roomId);
    }

    private onStoreUpdate = async (): Promise<void> => {
        const beforeRoomIds = this.roomIds;
        // clone the set as it may be mutated by the space store internally
        this.roomIds = new Set(SpaceStore.instance.getSpaceFilteredRoomIds(this.space));

        if (setHasDiff(beforeRoomIds, this.roomIds)) {
            this.emit(FILTER_CHANGED);
            // XXX: Room List Store has a bug where updates to the pre-filter during a local echo of a
            // tags transition seem to be ignored, so refire in the next tick to work around it
            setImmediate(() => {
                this.emit(FILTER_CHANGED);
            });
        }
    };

    public updateSpace(space: SpaceKey) {
        SpaceStore.instance.off(this.space, this.onStoreUpdate);
        SpaceStore.instance.on(this.space = space, this.onStoreUpdate);
        this.onStoreUpdate(); // initial update from the change to the space
    }

    public destroy(): void {
        SpaceStore.instance.off(this.space, this.onStoreUpdate);
    }
}
