import React from 'react';
import { MatrixEvent } from 'matrix-js-sdk/src';
import classNames from 'classnames';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { MatrixClientPeg } from "../../../MatrixClientPeg";

interface IProps {
    mxEvent: MatrixEvent;
}

interface IState {
    expanded: boolean;
}

@replaceableComponent("views.messages.ViewSourceEvent")
export default class ViewSourceEvent extends React.PureComponent<IProps, IState> {
    constructor(props) {
        super(props);

        this.state = {
            expanded: false,
        };
    }

    public componentDidMount(): void {
        const { mxEvent } = this.props;

        const client = MatrixClientPeg.get();
        client.decryptEventIfNeeded(mxEvent);

        if (mxEvent.isBeingDecrypted()) {
            mxEvent.once("Event.decrypted", () => this.forceUpdate());
        }
    }

    private onToggle = (ev: React.MouseEvent) => {
        ev.preventDefault();
        const { expanded } = this.state;
        this.setState({
            expanded: !expanded,
        });
    };

    public render(): React.ReactNode {
        const { mxEvent } = this.props;
        const { expanded } = this.state;

        let content;
        if (expanded) {
            content = <pre>{ JSON.stringify(mxEvent, null, 4) }</pre>;
        } else {
            content = <code>{ `{ "type": ${mxEvent.getType()} }` }</code>;
        }

        const classes = classNames("mx_ViewSourceEvent mx_EventTile_content", {
            mx_ViewSourceEvent_expanded: expanded,
        });

        return <span className={classes}>
            { content }
            <a
                className="mx_ViewSourceEvent_toggle"
                href="#"
                onClick={this.onToggle}
            />
        </span>;
    }
}
