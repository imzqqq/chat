import SettingsHandler from "./SettingsHandler";
import { SettingLevel } from "../SettingLevel";
import { WatchManager } from "../WatchManager";

/**
 * Gets and sets settings at the "room-device" level for the current device in a particular
 * room.
 */
export default class RoomDeviceSettingsHandler extends SettingsHandler {
    constructor(private watchers: WatchManager) {
        super();
    }

    public getValue(settingName: string, roomId: string): any {
        // Special case blacklist setting to use legacy values
        if (settingName === "blacklistUnverifiedDevices") {
            const value = this.read("mx_local_settings");
            if (value && value['blacklistUnverifiedDevicesPerRoom']) {
                return value['blacklistUnverifiedDevicesPerRoom'][roomId];
            }
        }

        const value = this.read(this.getKey(settingName, roomId));
        if (value) return value.value;
        return null;
    }

    public setValue(settingName: string, roomId: string, newValue: any): Promise<void> {
        // Special case blacklist setting for legacy structure
        if (settingName === "blacklistUnverifiedDevices") {
            let value = this.read("mx_local_settings");
            if (!value) value = {};
            if (!value["blacklistUnverifiedDevicesPerRoom"]) value["blacklistUnverifiedDevicesPerRoom"] = {};
            value["blacklistUnverifiedDevicesPerRoom"][roomId] = newValue;
            localStorage.setItem("mx_local_settings", JSON.stringify(value));
            this.watchers.notifyUpdate(settingName, roomId, SettingLevel.ROOM_DEVICE, newValue);
            return Promise.resolve();
        }

        if (newValue === null) {
            localStorage.removeItem(this.getKey(settingName, roomId));
        } else {
            newValue = JSON.stringify({ value: newValue });
            localStorage.setItem(this.getKey(settingName, roomId), newValue);
        }

        this.watchers.notifyUpdate(settingName, roomId, SettingLevel.ROOM_DEVICE, newValue);
        return Promise.resolve();
    }

    public canSetValue(settingName: string, roomId: string): boolean {
        return true; // It's their device, so they should be able to
    }

    public isSupported(): boolean {
        return localStorage !== undefined && localStorage !== null;
    }

    private read(key: string): any {
        const rawValue = localStorage.getItem(key);
        if (!rawValue) return null;
        return JSON.parse(rawValue);
    }

    private getKey(settingName: string, roomId: string): string {
        return "mx_setting_" + settingName + "_" + roomId;
    }
}
