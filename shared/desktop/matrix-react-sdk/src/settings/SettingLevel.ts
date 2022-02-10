/**
 * Represents the various setting levels supported by the SettingsStore.
 */
export enum SettingLevel {
    // TODO: [TS] Follow naming convention
    DEVICE = "device",
    ROOM_DEVICE = "room-device",
    ROOM_ACCOUNT = "room-account",
    ACCOUNT = "account",
    ROOM = "room",
    CONFIG = "config",
    DEFAULT = "default",
}
