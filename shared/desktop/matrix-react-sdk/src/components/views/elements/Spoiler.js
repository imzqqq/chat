import React from 'react';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.elements.Spoiler")
export default class Spoiler extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            visible: false,
        };
    }

    toggleVisible(e) {
        if (!this.state.visible) {
            // we are un-blurring, we don't want this click to propagate to potential child pills
            e.preventDefault();
            e.stopPropagation();
        }
        this.setState({ visible: !this.state.visible });
    }

    render() {
        const reason = this.props.reason ? (
            <span className="mx_EventTile_spoiler_reason">{ "(" + this.props.reason + ")" }</span>
        ) : null;
        // react doesn't allow appending a DOM node as child.
        // as such, we pass the this.props.contentHtml instead and then set the raw
        // HTML content. This is secure as the contents have already been parsed previously
        return (
            <span className={"mx_EventTile_spoiler" + (this.state.visible ? " visible" : "")} onClick={this.toggleVisible.bind(this)}>
                { reason }
                &nbsp;
                <span className="mx_EventTile_spoiler_content" dangerouslySetInnerHTML={{ __html: this.props.contentHtml }} />
            </span>
        );
    }
}
