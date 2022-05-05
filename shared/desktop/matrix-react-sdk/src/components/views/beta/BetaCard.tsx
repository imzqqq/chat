import React from "react";
import classNames from "classnames";

import { _t } from "../../../languageHandler";
import AccessibleButton from "../elements/AccessibleButton";
import SettingsStore from "../../../settings/SettingsStore";
import { SettingLevel } from "../../../settings/SettingLevel";
import TextWithTooltip from "../elements/TextWithTooltip";
import Modal from "../../../Modal";
import BetaFeedbackDialog from "../dialogs/BetaFeedbackDialog";
import SdkConfig from "../../../SdkConfig";
import SettingsFlag from "../elements/SettingsFlag";

// XXX: Keep this around for re-use in future Betas

interface IProps {
    title?: string;
    featureId: string;
}

export const BetaPill = ({ onClick }: { onClick?: () => void }) => {
    if (onClick) {
        return <TextWithTooltip
            class={classNames("mx_BetaCard_betaPill", {
                mx_BetaCard_betaPill_clickable: !!onClick,
            })}
            tooltip={<div>
                <div className="mx_Tooltip_title">
                    { _t("Spaces is a beta feature") }
                </div>
                <div className="mx_Tooltip_sub">
                    { _t("Tap for more info") }
                </div>
            </div>}
            onClick={onClick}
            tooltipProps={{ yOffset: -10 }}
        >
            { _t("Beta") }
        </TextWithTooltip>;
    }

    return <span
        className={classNames("mx_BetaCard_betaPill", {
            mx_BetaCard_betaPill_clickable: !!onClick,
        })}
        onClick={onClick}
    >
        { _t("Beta") }
    </span>;
};

const BetaCard = ({ title: titleOverride, featureId }: IProps) => {
    const info = SettingsStore.getBetaInfo(featureId);
    if (!info) return null; // Beta is invalid/disabled

    const { title, caption, disclaimer, image, feedbackLabel, feedbackSubheading, extraSettings } = info;
    const value = SettingsStore.getValue(featureId);

    let feedbackButton;
    if (value && feedbackLabel && feedbackSubheading && SdkConfig.get().bug_report_endpoint_url) {
        feedbackButton = <AccessibleButton
            onClick={() => {
                Modal.createTrackedDialog("Beta Feedback", featureId, BetaFeedbackDialog, { featureId });
            }}
            kind="primary"
        >
            { _t("Feedback") }
        </AccessibleButton>;
    }

    return <div className="mx_BetaCard">
        <div className="mx_BetaCard_columns">
            <div>
                <h3 className="mx_BetaCard_title">
                    { titleOverride || _t(title) }
                    <BetaPill />
                </h3>
                <span className="mx_BetaCard_caption">{ _t(caption) }</span>
                <div className="mx_BetaCard_buttons">
                    { feedbackButton }
                    <AccessibleButton
                        onClick={() => SettingsStore.setValue(featureId, null, SettingLevel.DEVICE, !value)}
                        kind={feedbackButton ? "primary_outline" : "primary"}
                    >
                        { value ? _t("Leave the beta") : _t("Join the beta") }
                    </AccessibleButton>
                </div>
                { disclaimer && <div className="mx_BetaCard_disclaimer">
                    { disclaimer(value) }
                </div> }
            </div>
            <img src={image} alt="" />
        </div>
        { extraSettings && value && <div className="mx_BetaCard_relatedSettings">
            { extraSettings.map(key => (
                <SettingsFlag key={key} name={key} level={SettingLevel.DEVICE} />
            )) }
        </div> }
    </div>;
};

export default BetaCard;
