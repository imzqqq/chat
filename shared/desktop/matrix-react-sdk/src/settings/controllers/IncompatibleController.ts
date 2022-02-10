import SettingController from "./SettingController";
import { SettingLevel } from "../SettingLevel";
import SettingsStore from "../SettingsStore";

/**
 * Enforces that a boolean setting cannot be enabled if the incompatible setting
 * is also enabled, to prevent cascading undefined behaviour between conflicting
 * labs flags.
 */
export default class IncompatibleController extends SettingController {
    public constructor(
        private settingName: string,
        private forcedValue: any = false,
        private incompatibleValue: any = true,
    ) {
        super();
    }

    public getValueOverride(
        level: SettingLevel,
        roomId: string,
        calculatedValue: any,
        calculatedAtLevel: SettingLevel,
    ): any {
        if (this.incompatibleSetting) {
            return this.forcedValue;
        }
        return null; // no override
    }

    public get settingDisabled(): boolean {
        return this.incompatibleSetting;
    }

    public get incompatibleSetting(): boolean {
        return SettingsStore.getValue(this.settingName) === this.incompatibleValue;
    }
}
