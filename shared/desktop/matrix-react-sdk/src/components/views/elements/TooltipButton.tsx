import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import Tooltip from './Tooltip';

interface IProps {
    helpText: React.ReactNode | string;
}

interface IState {
    hover: boolean;
}

@replaceableComponent("views.elements.TooltipButton")
export default class TooltipButton extends React.Component<IProps, IState> {
    constructor(props) {
        super(props);
        this.state = {
            hover: false,
        };
    }

    private onMouseOver = () => {
        this.setState({
            hover: true,
        });
    };

    private onMouseLeave = () => {
        this.setState({
            hover: false,
        });
    };

    render() {
        const tip = this.state.hover ? <Tooltip
            className="mx_TooltipButton_container"
            tooltipClassName="mx_TooltipButton_helpText"
            label={this.props.helpText}
        /> : <div />;
        return (
            <div className="mx_TooltipButton" onMouseOver={this.onMouseOver} onMouseLeave={this.onMouseLeave}>
                ?
                { tip }
            </div>
        );
    }
}
