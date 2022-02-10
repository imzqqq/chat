import React, { useState } from "react";
import PropTypes from "prop-types";
import classNames from 'classnames';

import { _t, _td } from '../../../languageHandler';
import AccessibleButton from "../elements/AccessibleButton";
import Tooltip from "../elements/Tooltip";

export const E2E_STATE = {
    VERIFIED: "verified",
    WARNING: "warning",
    UNKNOWN: "unknown",
    NORMAL: "normal",
    UNAUTHENTICATED: "unauthenticated",
};

const crossSigningUserTitles = {
    [E2E_STATE.WARNING]: _td("This user has not verified all of their sessions."),
    [E2E_STATE.NORMAL]: _td("You have not verified this user."),
    [E2E_STATE.VERIFIED]: _td("You have verified this user. This user has verified all of their sessions."),
};
const crossSigningRoomTitles = {
    [E2E_STATE.WARNING]: _td("Someone is using an unknown session"),
    [E2E_STATE.NORMAL]: _td("This room is end-to-end encrypted"),
    [E2E_STATE.VERIFIED]: _td("Everyone in this room is verified"),
};

const E2EIcon = ({ isUser, status, className, size, onClick, hideTooltip, bordered }) => {
    const [hover, setHover] = useState(false);

    const classes = classNames({
        mx_E2EIcon: true,
        mx_E2EIcon_bordered: bordered,
        mx_E2EIcon_warning: status === E2E_STATE.WARNING,
        mx_E2EIcon_normal: status === E2E_STATE.NORMAL,
        mx_E2EIcon_verified: status === E2E_STATE.VERIFIED,
    }, className);

    let e2eTitle;
    if (isUser) {
        e2eTitle = crossSigningUserTitles[status];
    } else {
        e2eTitle = crossSigningRoomTitles[status];
    }

    let style;
    if (size) {
        style = { width: `${size}px`, height: `${size}px` };
    }

    const onMouseOver = () => setHover(true);
    const onMouseLeave = () => setHover(false);

    let tip;
    if (hover && !hideTooltip) {
        tip = <Tooltip label={e2eTitle ? _t(e2eTitle) : ""} />;
    }

    if (onClick) {
        return (
            <AccessibleButton
                onClick={onClick}
                onMouseOver={onMouseOver}
                onMouseLeave={onMouseLeave}
                className={classes}
                style={style}
            >
                { tip }
            </AccessibleButton>
        );
    }

    return <div onMouseOver={onMouseOver} onMouseLeave={onMouseLeave} className={classes} style={style}>
        { tip }
    </div>;
};

E2EIcon.propTypes = {
    isUser: PropTypes.bool,
    status: PropTypes.oneOf(Object.values(E2E_STATE)),
    className: PropTypes.string,
    size: PropTypes.number,
    onClick: PropTypes.func,
};

export default E2EIcon;
