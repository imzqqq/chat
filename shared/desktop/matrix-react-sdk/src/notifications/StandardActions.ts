import { NotificationUtils } from "./NotificationUtils";

const encodeActions = NotificationUtils.encodeActions;

export class StandardActions {
    static ACTION_NOTIFY = encodeActions({ notify: true });
    static ACTION_NOTIFY_DEFAULT_SOUND = encodeActions({ notify: true, sound: "default" });
    static ACTION_NOTIFY_RING_SOUND = encodeActions({ notify: true, sound: "ring" });
    static ACTION_HIGHLIGHT = encodeActions({ notify: true, highlight: true });
    static ACTION_HIGHLIGHT_DEFAULT_SOUND = encodeActions({ notify: true, sound: "default", highlight: true });
    static ACTION_DONT_NOTIFY = encodeActions({ notify: false });
    static ACTION_DISABLED = null;
}
