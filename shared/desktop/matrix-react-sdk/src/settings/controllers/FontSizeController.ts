import SettingController from "./SettingController";
import dis from "../../dispatcher/dispatcher";
import { UpdateFontSizePayload } from "../../dispatcher/payloads/UpdateFontSizePayload";
import { Action } from "../../dispatcher/actions";
import { SettingLevel } from "../SettingLevel";

export default class FontSizeController extends SettingController {
    constructor() {
        super();
    }

    public onChange(level: SettingLevel, roomId: string, newValue: any) {
        // Dispatch font size change so that everything open responds to the change.
        dis.dispatch<UpdateFontSizePayload>({
            action: Action.UpdateFontSize,
            size: newValue,
        });
    }
}
