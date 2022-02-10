import React from "react";

import { _t } from '../../../languageHandler';
import { IDialogProps } from "./IDialogProps";
import SettingsStore from "../../../settings/SettingsStore";
import AccessibleButton from "../elements/AccessibleButton";
import defaultDispatcher from "../../../dispatcher/dispatcher";
import { Action } from "../../../dispatcher/actions";
import { UserTab } from "./UserSettingsDialog";
import GenericFeatureFeedbackDialog from "./GenericFeatureFeedbackDialog";

// XXX: Keep this around for re-use in future Betas

interface IProps extends IDialogProps {
    featureId: string;
}

const BetaFeedbackDialog: React.FC<IProps> = ({ featureId, onFinished }) => {
    const info = SettingsStore.getBetaInfo(featureId);

    return <GenericFeatureFeedbackDialog
        title={_t("%(featureName)s beta feedback", { featureName: info.title })}
        subheading={_t(info.feedbackSubheading)}
        onFinished={onFinished}
        rageshakeLabel={info.feedbackLabel}
        rageshakeData={Object.fromEntries((SettingsStore.getBetaInfo(featureId)?.extraSettings || []).map(k => {
            return SettingsStore.getValue(k);
        }))}
    >
        <AccessibleButton
            kind="link"
            onClick={() => {
                onFinished(false);
                defaultDispatcher.dispatch({
                    action: Action.ViewUserSettings,
                    initialTabId: UserTab.Labs,
                });
            }}
        >
            { _t("To leave the beta, visit your settings.") }
        </AccessibleButton>
    </GenericFeatureFeedbackDialog>;
};

export default BetaFeedbackDialog;
