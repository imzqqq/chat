import React, { ReactNode } from 'react';
import classNames from 'classnames';

import AutoHideScrollbar from "../../structures/AutoHideScrollbar";
import { _t } from "../../../languageHandler";
import AccessibleButton from "../elements/AccessibleButton";
import defaultDispatcher from "../../../dispatcher/dispatcher";
import { SetRightPanelPhasePayload } from "../../../dispatcher/payloads/SetRightPanelPhasePayload";
import { Action } from "../../../dispatcher/actions";
import { RightPanelPhases } from "../../../stores/RightPanelStorePhases";

interface IProps {
    header?: ReactNode;
    footer?: ReactNode;
    className?: string;
    withoutScrollContainer?: boolean;
    previousPhase?: RightPanelPhases;
    closeLabel?: string;
    onClose?(): void;
    refireParams?;
}

interface IGroupProps {
    className?: string;
    title: string;
}

export const Group: React.FC<IGroupProps> = ({ className, title, children }) => {
    return <div className={classNames("mx_BaseCard_Group", className)}>
        <h1>{ title }</h1>
        { children }
    </div>;
};

const BaseCard: React.FC<IProps> = ({
    closeLabel,
    onClose,
    className,
    header,
    footer,
    withoutScrollContainer,
    previousPhase,
    children,
    refireParams,
}) => {
    let backButton;
    if (previousPhase) {
        const onBackClick = () => {
            defaultDispatcher.dispatch<SetRightPanelPhasePayload>({
                action: Action.SetRightPanelPhase,
                phase: previousPhase,
                refireParams: refireParams,
            });
        };
        backButton = <AccessibleButton className="mx_BaseCard_back" onClick={onBackClick} title={_t("Back")} />;
    }

    let closeButton;
    if (onClose) {
        closeButton = <AccessibleButton
            className="mx_BaseCard_close"
            onClick={onClose}
            title={closeLabel || _t("Close")}
        />;
    }

    if (!withoutScrollContainer) {
        children = <AutoHideScrollbar>
            { children }
        </AutoHideScrollbar>;
    }

    return (
        <div className={classNames("mx_BaseCard", className)}>
            <div className="mx_BaseCard_header">
                { backButton }
                { closeButton }
                { header }
            </div>
            { children }
            { footer && <div className="mx_BaseCard_footer">{ footer }</div> }
        </div>
    );
};

export default BaseCard;
