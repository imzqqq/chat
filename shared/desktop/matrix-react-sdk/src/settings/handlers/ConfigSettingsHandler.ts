import SettingsHandler from "./SettingsHandler";
import SdkConfig from "../../SdkConfig";
import { isNullOrUndefined } from "matrix-js-sdk/src/utils";
import { Theme } from "../Theme";

/**
 * Gets and sets settings at the "config" level. This handler does not make use of the
 * roomId parameter.
 */
export default class ConfigSettingsHandler extends SettingsHandler {
    public constructor(private featureNames: string[]) {
        super();
    }

    public getValue(settingName: string, roomId: string): any {
        const config = SdkConfig.get() || {};

        if (this.featureNames.includes(settingName)) {
            const labsConfig = config["features"] || {};
            const val = labsConfig[settingName];
            if (isNullOrUndefined(val)) return null; // no definition at this level
            if (val === true || val === false) return val; // new style: mapped as a boolean
            if (val === "enable") return true; // backwards compat
            if (val === "disable") return false; // backwards compat
            if (val === "labs") return null; // backwards compat, no override
            return null; // fallback in the case of invalid input
        }

        // Special case themes
        if (settingName === "theme_in_use" && config["default_theme"]) {
            if (config["default_theme"] === "light") return Theme.Light;
            if (config["default_theme"] === "dark") return Theme.Dark;
            if (config["default_theme"] === "system") Theme.System;
        }

        const settingsConfig = config["settingDefaults"];
        if (!settingsConfig || isNullOrUndefined(settingsConfig[settingName])) return null;
        return settingsConfig[settingName];
    }

    public async setValue(settingName: string, roomId: string, newValue: any): Promise<void> {
        throw new Error("Cannot change settings at the config level");
    }

    public canSetValue(settingName: string, roomId: string): boolean {
        return false;
    }

    public isSupported(): boolean {
        return true; // SdkConfig is always there
    }
}
