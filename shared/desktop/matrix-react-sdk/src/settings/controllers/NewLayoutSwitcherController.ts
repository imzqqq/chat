import SettingController from "./SettingController";
import { SettingLevel } from "../SettingLevel";
import SettingsStore from "../SettingsStore";
import { Layout } from "../Layout";

export default class NewLayoutSwitcherController extends SettingController {
    public onChange(level: SettingLevel, roomId: string, newValue: any) {
        // On disabling switch back to Layout.Group if Layout.Bubble
        if (!newValue && SettingsStore.getValue("layout") == Layout.Bubble) {
            SettingsStore.setValue("layout", null, SettingLevel.DEVICE, Layout.Group);
        }
    }
}
