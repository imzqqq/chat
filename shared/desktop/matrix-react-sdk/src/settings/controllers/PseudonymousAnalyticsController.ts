import SettingController from "./SettingController";
import { SettingLevel } from "../SettingLevel";
import { PosthogAnalytics } from "../../PosthogAnalytics";
import { MatrixClientPeg } from "../../MatrixClientPeg";

export default class PseudonymousAnalyticsController extends SettingController {
    public onChange(level: SettingLevel, roomId: string, newValue: any) {
        PosthogAnalytics.instance.updateAnonymityFromSettings(MatrixClientPeg.get().getUserId());
    }
}
