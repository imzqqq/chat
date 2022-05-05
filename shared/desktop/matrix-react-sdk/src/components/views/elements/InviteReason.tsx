import classNames from "classnames";
import React from "react";
import { sanitizedHtmlNode } from "../../../HtmlUtils";
import { _t } from "../../../languageHandler";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    reason: string;
    htmlReason?: string;
}

interface IState {
    hidden: boolean;
}

@replaceableComponent("views.elements.InviteReason")
export default class InviteReason extends React.PureComponent<IProps, IState> {
    constructor(props) {
        super(props);
        this.state = {
            // We hide the reason for invitation by default, since it can be a
            // vector for spam/harassment.
            hidden: true,
        };
    }

    onViewClick = () => {
        this.setState({
            hidden: false,
        });
    };

    render() {
        const classes = classNames({
            "mx_InviteReason": true,
            "mx_InviteReason_hidden": this.state.hidden,
        });

        return <div className={classes}>
            <div className="mx_InviteReason_reason">{ this.props.htmlReason ? sanitizedHtmlNode(this.props.htmlReason) : this.props.reason }</div>
            <div className="mx_InviteReason_view"
                onClick={this.onViewClick}
            >
                { _t("View message") }
            </div>
        </div>;
    }
}
