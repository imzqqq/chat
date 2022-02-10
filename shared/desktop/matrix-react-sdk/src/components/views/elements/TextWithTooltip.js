import React from 'react';
import PropTypes from 'prop-types';
import * as sdk from '../../../index';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.elements.TextWithTooltip")
export default class TextWithTooltip extends React.Component {
    static propTypes = {
        class: PropTypes.string,
        tooltipClass: PropTypes.string,
        tooltip: PropTypes.node.isRequired,
        tooltipProps: PropTypes.object,
    };

    constructor() {
        super();

        this.state = {
            hover: false,
        };
    }

    onMouseOver = () => {
        this.setState({ hover: true });
    };

    onMouseLeave = () => {
        this.setState({ hover: false });
    };

    render() {
        const Tooltip = sdk.getComponent("elements.Tooltip");

        const { class: className, children, tooltip, tooltipClass, tooltipProps, ...props } = this.props;

        return (
            <span {...props} onMouseOver={this.onMouseOver} onMouseLeave={this.onMouseLeave} className={className}>
                { children }
                { this.state.hover && <Tooltip
                    {...tooltipProps}
                    label={tooltip}
                    tooltipClassName={tooltipClass}
                    className="mx_TextWithTooltip_tooltip"
                /> }
            </span>
        );
    }
}
