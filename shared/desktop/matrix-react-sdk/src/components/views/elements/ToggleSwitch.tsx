import React from "react";
import classNames from "classnames";
import AccessibleButton from "./AccessibleButton";

interface IProps {
    // Whether or not this toggle is in the 'on' position.
    checked: boolean;

    // Whether or not the user can interact with the switch
    disabled: boolean;

    // Called when the checked state changes. First argument will be the new state.
    onChange(checked: boolean): void;
}

// Controlled Toggle Switch element, written with Accessibility in mind
export default ({ checked, disabled = false, onChange, ...props }: IProps) => {
    const _onClick = () => {
        if (disabled) return;
        onChange(!checked);
    };

    const classes = classNames({
        "mx_ToggleSwitch": true,
        "mx_ToggleSwitch_on": checked,
        "mx_ToggleSwitch_enabled": !disabled,
    });

    return (
        <AccessibleButton {...props}
            className={classes}
            onClick={_onClick}
            role="switch"
            aria-checked={checked}
            aria-disabled={disabled}
        >
            <div className="mx_ToggleSwitch_ball" />
        </AccessibleButton>
    );
};
