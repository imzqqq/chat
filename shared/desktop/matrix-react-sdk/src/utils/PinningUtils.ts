import { MatrixEvent } from "matrix-js-sdk/src/models/event";

export default class PinningUtils {
    /**
     * Determines if the given event may be pinned.
     * @param {MatrixEvent} event The event to check.
     * @return {boolean} True if the event may be pinned, false otherwise.
     */
    static isPinnable(event: MatrixEvent): boolean {
        if (!event) return false;
        if (event.getType() !== "m.room.message") return false;
        if (event.isRedacted()) return false;

        return true;
    }
}
