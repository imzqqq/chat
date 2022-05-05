import SettingsHandler from "./SettingsHandler";

/**
 * A wrapper for a SettingsHandler that performs local echo on
 * changes to settings. This wrapper will use the underlying
 * handler as much as possible to ensure values are not stale.
 */
export default class LocalEchoWrapper extends SettingsHandler {
    private cache: {
        [settingName: string]: {
            [roomId: string]: any;
        };
    } = {};

    /**
     * Creates a new local echo wrapper
     * @param {SettingsHandler} handler The handler to wrap
     */
    constructor(private handler: SettingsHandler) {
        super();
    }

    public getValue(settingName: string, roomId: string): any {
        const cacheRoomId = roomId ? roomId : "UNDEFINED"; // avoid weird keys
        const bySetting = this.cache[settingName];
        if (bySetting && bySetting.hasOwnProperty(cacheRoomId)) {
            return bySetting[cacheRoomId];
        }

        return this.handler.getValue(settingName, roomId);
    }

    public setValue(settingName: string, roomId: string, newValue: any): Promise<void> {
        if (!this.cache[settingName]) this.cache[settingName] = {};
        const bySetting = this.cache[settingName];

        const cacheRoomId = roomId ? roomId : "UNDEFINED"; // avoid weird keys
        bySetting[cacheRoomId] = newValue;

        const handlerPromise = this.handler.setValue(settingName, roomId, newValue);
        return Promise.resolve(handlerPromise).finally(() => {
            delete bySetting[cacheRoomId];
        });
    }

    public canSetValue(settingName: string, roomId: string): boolean {
        return this.handler.canSetValue(settingName, roomId);
    }

    public isSupported(): boolean {
        return this.handler.isSupported();
    }
}
