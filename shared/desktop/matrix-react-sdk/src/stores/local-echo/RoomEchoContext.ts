import { EchoContext } from "./EchoContext";
import { Room } from "matrix-js-sdk/src/models/room";

export class RoomEchoContext extends EchoContext {
    constructor(public readonly room: Room) {
        super();
    }
}
