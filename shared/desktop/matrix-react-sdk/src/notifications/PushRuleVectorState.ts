import { StandardActions } from "./StandardActions";
import { NotificationUtils } from "./NotificationUtils";
import { IPushRule } from "matrix-js-sdk/src/@types/PushRules";

export enum VectorState {
    /** The push rule is disabled */
    Off = "off",
    /** The user will receive push notification for this rule */
    On = "on",
    /** The user will receive push notification for this rule with sound and
     highlight if this is legitimate */
    Loud = "loud",
}

export class PushRuleVectorState {
    // Backwards compatibility (things should probably be using the enum above instead)
    static OFF = VectorState.Off;
    static ON = VectorState.On;
    static LOUD = VectorState.Loud;

    /**
     * Enum for state of a push rule as defined by the Vector UI.
     * @readonly
     * @enum {string}
     */
    static states = VectorState;

    /**
     * Convert a PushRuleVectorState to a list of actions
     *
     * @return [object] list of push-rule actions
     */
    static actionsFor(pushRuleVectorState: VectorState) {
        if (pushRuleVectorState === VectorState.On) {
            return StandardActions.ACTION_NOTIFY;
        } else if (pushRuleVectorState === VectorState.Loud) {
            return StandardActions.ACTION_HIGHLIGHT_DEFAULT_SOUND;
        }
    }

    /**
     * Convert a pushrule's actions to a PushRuleVectorState.
     *
     * Determines whether a content rule is in the PushRuleVectorState.ON
     * category or in PushRuleVectorState.LOUD, regardless of its enabled
     * state. Returns null if it does not match these categories.
     */
    static contentRuleVectorStateKind(rule: IPushRule): VectorState {
        const decoded = NotificationUtils.decodeActions(rule.actions);

        if (!decoded) {
            return null;
        }

        // Count tweaks to determine if it is a ON or LOUD rule
        let tweaks = 0;
        if (decoded.sound) {
            tweaks++;
        }
        if (decoded.highlight) {
            tweaks++;
        }
        let stateKind = null;
        switch (tweaks) {
            case 0:
                stateKind = VectorState.On;
                break;
            case 2:
                stateKind = VectorState.Loud;
                break;
        }
        return stateKind;
    }
}
