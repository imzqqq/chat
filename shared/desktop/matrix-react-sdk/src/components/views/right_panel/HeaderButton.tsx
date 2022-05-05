import React from 'react';
import classNames from 'classnames';
import Analytics from '../../../Analytics';
import AccessibleTooltipButton from "../elements/AccessibleTooltipButton";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    // Whether this button is highlighted
    isHighlighted: boolean;
    // click handler
    onClick: () => void;
    // The parameters to track the click event
    analytics: Parameters<typeof Analytics.trackEvent>;

    // Button name
    name: string;
    // Button title
    title: string;
}

// TODO: replace this, the composer buttons and the right panel buttons with a unified representation
@replaceableComponent("views.right_panel.HeaderButton")
export default class HeaderButton extends React.Component<IProps> {
    private onClick = () => {
        Analytics.trackEvent(...this.props.analytics);
        this.props.onClick();
    };

    public render() {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const { isHighlighted, onClick, analytics, name, title, ...props } = this.props;

        const classes = classNames({
            mx_RightPanel_headerButton: true,
            mx_RightPanel_headerButton_highlight: isHighlighted,
            [`mx_RightPanel_${name}`]: true,
        });

        return <AccessibleTooltipButton
            {...props}
            aria-selected={isHighlighted}
            role="tab"
            title={title}
            className={classes}
            onClick={this.onClick}
        />;
    }
}
