import { MatrixClientPeg } from '../../MatrixClientPeg';
import { SettingLevel } from "../SettingLevel";
import SettingController from "./SettingController";

/**
 * When the value changes, call a setter function on the matrix client with the new value
 */
export default class PushToMatrixClientController extends SettingController {
    constructor(private setter: Function, private inverse: boolean) {
        super();
    }

    public onChange(level: SettingLevel, roomId: string, newValue: any) {
        // XXX does this work? This surely isn't necessarily the effective value,
        // but it's what NotificationsEnabledController does...
        this.setter.call(MatrixClientPeg.get(), this.inverse ? !newValue : newValue);
    }
}
