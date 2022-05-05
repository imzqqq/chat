import React from "react";

import ToggleSwitch from "./ToggleSwitch";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    // The value for the toggle switch
    value: boolean;
    // The translated label for the switch
    label: string;
    // Whether or not to disable the toggle switch
    disabled?: boolean;
    // True to put the toggle in front of the label
    // Default false.
    toggleInFront?: boolean;
    // Additional class names to append to the switch. Optional.
    className?: string;
    // The function to call when the value changes
    onChange(checked: boolean): void;
}

@replaceableComponent("views.elements.LabelledToggleSwitch")
export default class LabelledToggleSwitch extends React.PureComponent<IProps> {
    render() {
        // This is a minimal version of a SettingsFlag

        let firstPart = <span className="mx_SettingsFlag_label">{ this.props.label }</span>;
        let secondPart = <ToggleSwitch
            checked={this.props.value}
            disabled={this.props.disabled}
            onChange={this.props.onChange}
            aria-label={this.props.label}
        />;

        if (this.props.toggleInFront) {
            const temp = firstPart;
            firstPart = secondPart;
            secondPart = temp;
        }

        const classes = `mx_SettingsFlag ${this.props.className || ""}`;
        return (
            <div className={classes}>
                { firstPart }
                { secondPart }
            </div>
        );
    }
}
