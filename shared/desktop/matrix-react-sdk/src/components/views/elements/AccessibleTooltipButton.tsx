import React from 'react';
import classNames from 'classnames';

import AccessibleButton from "./AccessibleButton";
import Tooltip, { Alignment } from './Tooltip';
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface ITooltipProps extends React.ComponentProps<typeof AccessibleButton> {
    title: string;
    tooltip?: React.ReactNode;
    label?: React.ReactNode;
    tooltipClassName?: string;
    forceHide?: boolean;
    yOffset?: number;
    alignment?: Alignment;
}

interface IState {
    hover: boolean;
}

@replaceableComponent("views.elements.AccessibleTooltipButton")
export default class AccessibleTooltipButton extends React.PureComponent<ITooltipProps, IState> {
    constructor(props: ITooltipProps) {
        super(props);
        this.state = {
            hover: false,
        };
    }

    componentDidUpdate(prevProps: Readonly<ITooltipProps>) {
        if (!prevProps.forceHide && this.props.forceHide && this.state.hover) {
            this.setState({
                hover: false,
            });
        }
    }

    onMouseOver = () => {
        if (this.props.forceHide) return;
        this.setState({
            hover: true,
        });
    };

    onMouseLeave = () => {
        this.setState({
            hover: false,
        });
    };

    render() {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const { title, tooltip, children, tooltipClassName, forceHide, yOffset, alignment, ...props } = this.props;

        const tip = this.state.hover ? <Tooltip
            className="mx_AccessibleTooltipButton_container"
            tooltipClassName={classNames("mx_AccessibleTooltipButton_tooltip", tooltipClassName)}
            label={tooltip || title}
            yOffset={yOffset}
            alignment={alignment}
        /> : null;
        return (
            <AccessibleButton
                {...props}
                onMouseOver={this.onMouseOver}
                onMouseLeave={this.onMouseLeave}
                aria-label={title}
            >
                { children }
                { this.props.label }
                { (tooltip || title) && tip }
            </AccessibleButton>
        );
    }
}
