import { NotificationColor } from "./NotificationColor";
import { NotificationState } from "./NotificationState";

/**
 * Summarizes a number of states into a unique snapshot. To populate, call
 * the add() function with the notification states to be included.
 *
 * Useful for community notification counts, global notification counts, etc.
 */
export class SummarizedNotificationState extends NotificationState {
    private totalStatesWithUnread = 0;

    constructor() {
        super();
        this._symbol = null;
        this._count = 0;
        this._color = NotificationColor.None;
    }

    public get numUnreadStates(): number {
        return this.totalStatesWithUnread;
    }

    /**
     * Append a notification state to this snapshot, taking the loudest NotificationColor
     * of the two. By default this will not adopt the symbol of the other notification
     * state to prevent the count from being lost in typical usage.
     * @param other The other notification state to append.
     * @param includeSymbol If true, the notification state's symbol will be taken if one
     * is present.
     */
    public add(other: NotificationState, includeSymbol = false) {
        if (other.symbol && includeSymbol) {
            this._symbol = other.symbol;
        }
        if (other.count) {
            this._count += other.count;
        }
        if (other.color > this.color) {
            this._color = other.color;
        }
        if (other.hasUnreadCount) {
            this.totalStatesWithUnread++;
        }
    }
}
