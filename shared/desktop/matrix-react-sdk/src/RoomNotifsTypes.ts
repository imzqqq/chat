import {
    ALL_MESSAGES,
    ALL_MESSAGES_LOUD,
    MENTIONS_ONLY,
    MUTE,
} from "./RoomNotifs";

export type Volume = ALL_MESSAGES_LOUD | ALL_MESSAGES | MENTIONS_ONLY | MUTE;
