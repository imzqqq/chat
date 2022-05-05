import { NotificationColor } from "./NotificationColor";
import { NotificationState } from "./NotificationState";

export class StaticNotificationState extends NotificationState {
    public static readonly RED_EXCLAMATION = StaticNotificationState.forSymbol("!", NotificationColor.Red);

    constructor(symbol: string, count: number, color: NotificationColor) {
        super();
        this._symbol = symbol;
        this._count = count;
        this._color = color;
    }

    public static forCount(count: number, color: NotificationColor): StaticNotificationState {
        return new StaticNotificationState(null, count, color);
    }

    public static forSymbol(symbol: string, color: NotificationColor): StaticNotificationState {
        return new StaticNotificationState(symbol, 0, color);
    }
}
