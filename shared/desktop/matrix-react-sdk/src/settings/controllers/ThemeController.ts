import SettingController from "./SettingController";
import { enumerateThemes } from "../../theme";
import { SettingLevel } from "../SettingLevel";

export default class ThemeController extends SettingController {
    public static isLogin = false;

    public constructor(private defaultTheme: string) {
        super();
    }

    public getValueOverride(
        level: SettingLevel,
        roomId: string,
        calculatedValue: any,
        calculatedAtLevel: SettingLevel,
    ): any {
        if (!calculatedValue) return null; // Don't override null themes

        if (ThemeController.isLogin) return this.defaultTheme;

        const themes = enumerateThemes();
        // Override in case some no longer supported theme is stored here
        if (!themes[calculatedValue]) {
            return this.defaultTheme;
        }

        return null; // no override
    }
}
