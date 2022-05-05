import { Room } from "matrix-js-sdk/src/models/room";
import { TagID } from "../../models";
import { IAlgorithm } from "./IAlgorithm";
import { compare } from "../../../../utils/strings";

/**
 * Sorts rooms according to the browser's determination of alphabetic.
 */
export class AlphabeticAlgorithm implements IAlgorithm {
    public sortRooms(rooms: Room[], tagId: TagID): Room[] {
        return rooms.sort((a, b) => {
            return compare(a.name, b.name);
        });
    }
}
