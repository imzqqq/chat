import React from 'react';
import AccessibleButton from './AccessibleButton';
import dis from '../../../dispatcher/dispatcher';
import Analytics from '../../../Analytics';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import Tooltip from './Tooltip';

interface IProps {
    size?: string;
    tooltip?: boolean;
    action: string;
    mouseOverAction?: string;
    label: string;
    iconPath?: string;
    className?: string;
    children?: JSX.Chat;
}

interface IState {
    showTooltip: boolean;
}

@replaceableComponent("views.elements.ActionButton")
export default class ActionButton extends React.Component<IProps, IState> {
    static defaultProps: Partial<IProps> = {
        size: "25",
        tooltip: false,
    };

    constructor(props: IProps) {
        super(props);

        this.state = {
            showTooltip: false,
        };
    }

    private onClick = (ev: React.MouseEvent): void => {
        ev.stopPropagation();
        Analytics.trackEvent('Action Button', 'click', this.props.action);
        dis.dispatch({ action: this.props.action });
    };

    private onMouseEnter = (): void => {
        if (this.props.tooltip) this.setState({ showTooltip: true });
        if (this.props.mouseOverAction) {
            dis.dispatch({ action: this.props.mouseOverAction });
        }
    };

    private onMouseLeave = (): void => {
        this.setState({ showTooltip: false });
    };

    render() {
        let tooltip;
        if (this.state.showTooltip) {
            tooltip = <Tooltip className="mx_RoleButton_tooltip" label={this.props.label} />;
        }

        const icon = this.props.iconPath ?
            (<img src={this.props.iconPath} width={this.props.size} height={this.props.size} />) :
            undefined;

        const classNames = ["mx_RoleButton"];
        if (this.props.className) {
            classNames.push(this.props.className);
        }

        return (
            <AccessibleButton
                className={classNames.join(" ")}
                onClick={this.onClick}
                onMouseEnter={this.onMouseEnter}
                onMouseLeave={this.onMouseLeave}
                aria-label={this.props.label}
            >
                { icon }
                { tooltip }
                { this.props.children }
            </AccessibleButton>
        );
    }
}
