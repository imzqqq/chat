import SettingController from "./SettingController";
import { SettingLevel } from "../SettingLevel";

/**
 * For animation-like settings, this controller checks whether the user has
 * indicated they prefer reduced motion via browser or OS level settings.
 * If they have, this forces the setting value to false.
 */
export default class ReducedMotionController extends SettingController {
    public getValueOverride(
        level: SettingLevel,
        roomId: string,
        calculatedValue: any,
        calculatedAtLevel: SettingLevel,
    ): any {
        if (this.prefersReducedMotion()) {
            return false;
        }
        return null; // no override
    }

    public get settingDisabled(): boolean {
        return this.prefersReducedMotion();
    }

    private prefersReducedMotion(): boolean {
        return window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    }
}
