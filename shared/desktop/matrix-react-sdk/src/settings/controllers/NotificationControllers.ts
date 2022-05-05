import SettingController from "./SettingController";
import { MatrixClientPeg } from '../../MatrixClientPeg';
import { SettingLevel } from "../SettingLevel";

// XXX: This feels wrong.
import { PushProcessor } from "matrix-js-sdk/src/pushprocessor";
import { PushRuleActionName } from "matrix-js-sdk/src/@types/PushRules";

// .m.rule.master being enabled means all events match that push rule
// default action on this rule is dont_notify, but it could be something else
export function isPushNotifyDisabled(): boolean {
    // Return the value of the master push rule as a default
    const processor = new PushProcessor(MatrixClientPeg.get());
    const masterRule = processor.getPushRuleById(".m.rule.master");

    if (!masterRule) {
        console.warn("No master push rule! Notifications are disabled for this user.");
        return true;
    }

    // If the rule is enabled then check it does not notify on everything
    return masterRule.enabled && !masterRule.actions.includes(PushRuleActionName.Notify);
}

function getNotifier(): any { // TODO: [TS] Formal type that doesn't cause a cyclical reference.
    // eslint-disable-next-line @typescript-eslint/no-var-requires
    let Notifier = require('../../Notifier'); // avoids cyclical references
    if (Notifier.default) Notifier = Notifier.default; // correct for webpack require() weirdness
    return Notifier;
}

export class NotificationsEnabledController extends SettingController {
    public getValueOverride(
        level: SettingLevel,
        roomId: string,
        calculatedValue: any,
        calculatedAtLevel: SettingLevel,
    ): any {
        if (!getNotifier().isPossible()) return false;

        if (calculatedValue === null || calculatedAtLevel === "default") {
            return !isPushNotifyDisabled();
        }

        return calculatedValue;
    }

    public onChange(level: SettingLevel, roomId: string, newValue: any) {
        if (getNotifier().supportsDesktopNotifications()) {
            getNotifier().setEnabled(newValue);
        }
    }
}

export class NotificationBodyEnabledController extends SettingController {
    public getValueOverride(level: SettingLevel, roomId: string, calculatedValue: any): any {
        if (!getNotifier().isPossible()) return false;

        if (calculatedValue === null) {
            return !isPushNotifyDisabled();
        }

        return calculatedValue;
    }
}
