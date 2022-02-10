import { ActionPayload } from "../payloads";
import { Action } from "../actions";
import { SettingLevel } from "../../settings/SettingLevel";

export interface SettingUpdatedPayload extends ActionPayload {
    action: Action.SettingUpdated;

    settingName: string;
    roomId: string;
    level: SettingLevel;
    newValueAtLevel: SettingLevel;
    newValue: any;
}
