import SettingController from "./SettingController";
import PlatformPeg from "../../PlatformPeg";
import { SettingLevel } from "../SettingLevel";

export default class ReloadOnChangeController extends SettingController {
    public onChange(level: SettingLevel, roomId: string, newValue: any) {
        PlatformPeg.get().reload();
    }
}
