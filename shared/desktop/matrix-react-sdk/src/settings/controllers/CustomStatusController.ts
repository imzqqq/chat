import SettingController from "./SettingController";
import dis from "../../dispatcher/dispatcher";
import { SettingLevel } from "../SettingLevel";

export default class CustomStatusController extends SettingController {
    public onChange(level: SettingLevel, roomId: string, newValue: any) {
        // Dispatch setting change so that some components that are still visible when the
        // Settings page is open (such as RoomTiles) can reflect the change.
        dis.dispatch({
            action: "feature_custom_status_changed",
        });
    }
}
