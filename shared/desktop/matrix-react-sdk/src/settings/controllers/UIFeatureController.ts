import SettingController from "./SettingController";
import { SettingLevel } from "../SettingLevel";
import SettingsStore from "../SettingsStore";

/**
 * Enforces that a boolean setting cannot be enabled if the corresponding
 * UI feature is disabled. If the UI feature is enabled, the setting value
 * is unchanged.
 *
 * Settings using this controller are assumed to return `false` when disabled.
 */
export default class UIFeatureController extends SettingController {
    public constructor(private uiFeatureName: string, private forcedValue = false) {
        super();
    }

    public getValueOverride(
        level: SettingLevel,
        roomId: string,
        calculatedValue: any,
        calculatedAtLevel: SettingLevel,
    ): any {
        if (this.settingDisabled) {
            // per the docs: we force a disabled state when the feature isn't active
            return this.forcedValue;
        }
        return null; // no override
    }

    public get settingDisabled(): boolean {
        return !SettingsStore.getValue(this.uiFeatureName);
    }
}
