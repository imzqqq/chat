import React from 'react';
import classNames from 'classnames';

import Tooltip, { Alignment } from './Tooltip';
import { _t } from "../../../languageHandler";
import { replaceableComponent } from "../../../utils/replaceableComponent";

export enum InfoTooltipKind {
    Info = "info",
    Warning = "warning",
}

interface ITooltipProps {
    tooltip?: React.ReactNode;
    className?: string;
    tooltipClassName?: string;
    kind?: InfoTooltipKind;
}

interface IState {
    hover: boolean;
}

@replaceableComponent("views.elements.InfoTooltip")
export default class InfoTooltip extends React.PureComponent<ITooltipProps, IState> {
    constructor(props: ITooltipProps) {
        super(props);
        this.state = {
            hover: false,
        };
    }

    onMouseOver = () => {
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
        const { tooltip, children, tooltipClassName, className, kind } = this.props;
        const title = _t("Information");
        const iconClassName = (
            (kind !== InfoTooltipKind.Warning) ?
                "mx_InfoTooltip_icon_info" : "mx_InfoTooltip_icon_warning"
        );

        // Tooltip are forced on the right for a more natural feel to them on info icons
        const tip = this.state.hover ? <Tooltip
            className="mx_InfoTooltip_container"
            tooltipClassName={classNames("mx_InfoTooltip_tooltip", tooltipClassName)}
            label={tooltip || title}
            alignment={Alignment.Right}
        /> : <div />;
        return (
            <div
                onMouseOver={this.onMouseOver}
                onMouseLeave={this.onMouseLeave}
                className={classNames("mx_InfoTooltip", className)}
            >
                <span className={classNames("mx_InfoTooltip_icon", iconClassName)} aria-label={title} />
                { children }
                { tip }
            </div>
        );
    }
}
