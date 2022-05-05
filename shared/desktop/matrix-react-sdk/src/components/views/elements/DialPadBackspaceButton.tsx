import * as React from "react";
import AccessibleButton, { ButtonEvent } from "./AccessibleButton";

interface IProps {
    // Callback for when the button is pressed
    onBackspacePress: (ev: ButtonEvent) => void;
}

export default class DialPadBackspaceButton extends React.PureComponent<IProps> {
    render() {
        return <div className="mx_DialPadBackspaceButtonWrapper">
            <AccessibleButton className="mx_DialPadBackspaceButton" onClick={this.props.onBackspacePress} />
        </div>;
    }
}
