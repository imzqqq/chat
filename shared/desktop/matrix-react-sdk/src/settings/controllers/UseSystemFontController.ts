import SettingController from "./SettingController";
import SettingsStore from "../SettingsStore";
import dis from "../../dispatcher/dispatcher";
import { UpdateSystemFontPayload } from "../../dispatcher/payloads/UpdateSystemFontPayload";
import { Action } from "../../dispatcher/actions";
import { SettingLevel } from "../SettingLevel";

export default class UseSystemFontController extends SettingController {
    constructor() {
        super();
    }

    public onChange(level: SettingLevel, roomId: string, newValue: any) {
        // Dispatch font size change so that everything open responds to the change.
        dis.dispatch<UpdateSystemFontPayload>({
            action: Action.UpdateSystemFont,
            useSystemFont: newValue,
            font: SettingsStore.getValue("systemFont"),
        });
    }
}
