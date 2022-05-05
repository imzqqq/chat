import SettingController from "./SettingController";
import { SettingLevel } from "../SettingLevel";

/**
 * Allows for multiple controllers to affect a setting. The first controller
 * provided to this class which overrides the setting value will affect
 * the value - other controllers are not called. Change notification handlers
 * are proxied through to all controllers.
 *
 * Similarly, the first controller which indicates that a setting is disabled
 * will be used - other controllers will not be considered.
 */
export class OrderedMultiController extends SettingController {
    constructor(public readonly controllers: SettingController[]) {
        super();
    }

    public getValueOverride(
        level: SettingLevel,
        roomId: string,
        calculatedValue: any,
        calculatedAtLevel: SettingLevel,
    ): any {
        for (const controller of this.controllers) {
            const override = controller.getValueOverride(level, roomId, calculatedValue, calculatedAtLevel);
            if (override !== undefined && override !== null) return override;
        }
        return null; // no override
    }

    public onChange(level: SettingLevel, roomId: string, newValue: any) {
        for (const controller of this.controllers) {
            controller.onChange(level, roomId, newValue);
        }
    }

    public get settingDisabled(): boolean {
        for (const controller of this.controllers) {
            if (controller.settingDisabled) return true;
        }
        return false;
    }
}
