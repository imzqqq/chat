import { Room } from "matrix-js-sdk/src/models/room";
import { TagID } from "../../models";
import { IAlgorithm } from "./IAlgorithm";

/**
 * Sorts rooms according to the tag's `order` property on the room.
 */
export class ManualAlgorithm implements IAlgorithm {
    public sortRooms(rooms: Room[], tagId: TagID): Room[] {
        const getOrderProp = (r: Room) => r.tags[tagId].order || 0;
        return rooms.sort((a, b) => {
            return getOrderProp(a) - getOrderProp(b);
        });
    }
}
