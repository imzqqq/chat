import { RoomEchoChamber } from "./RoomEchoChamber";
import { Room } from "matrix-js-sdk/src/models/room";
import { EchoStore } from "./EchoStore";

/**
 * Semantic access to local echo
 */
export class EchoChamber {
    private constructor() {
    }

    public static forRoom(room: Room): RoomEchoChamber {
        return EchoStore.instance.getOrCreateChamberForRoom(room);
    }
}
