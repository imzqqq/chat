import React from "react";
import classNames from "classnames";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";

import { _t } from '../../../languageHandler';
import { formatCommaSeparatedList } from '../../../utils/FormattingUtils';
import dis from "../../../dispatcher/dispatcher";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import ReactionsRowButtonTooltip from "./ReactionsRowButtonTooltip";
import AccessibleButton from "../elements/AccessibleButton";
import MatrixClientContext from "../../../contexts/MatrixClientContext";

interface IProps {
    // The event we're displaying reactions for
    mxEvent: MatrixEvent;
    // The reaction content / key / emoji
    content: string;
    // The count of votes for this key
    count: number;
    // A Set of Chat reaction events for this key
    reactionEvents: Set<MatrixEvent>;
    // A possible Chat event if the current user has voted for this type
    myReactionEvent?: MatrixEvent;
}

interface IState {
    tooltipRendered: boolean;
    tooltipVisible: boolean;
}

@replaceableComponent("views.messages.ReactionsRowButton")
export default class ReactionsRowButton extends React.PureComponent<IProps, IState> {
    static contextType = MatrixClientContext;

    state = {
        tooltipRendered: false,
        tooltipVisible: false,
    };

    onClick = () => {
        const { mxEvent, myReactionEvent, content } = this.props;
        if (myReactionEvent) {
            this.context.redactEvent(
                mxEvent.getRoomId(),
                myReactionEvent.getId(),
            );
        } else {
            this.context.sendEvent(mxEvent.getRoomId(), "m.reaction", {
                "m.relates_to": {
                    "rel_type": "m.annotation",
                    "event_id": mxEvent.getId(),
                    "key": content,
                },
            });
            dis.dispatch({ action: "message_sent" });
        }
    };

    onMouseOver = () => {
        this.setState({
            // To avoid littering the DOM with a tooltip for every reaction,
            // only render it on first use.
            tooltipRendered: true,
            tooltipVisible: true,
        });
    };

    onMouseLeave = () => {
        this.setState({
            tooltipVisible: false,
        });
    };

    render() {
        const { mxEvent, content, count, reactionEvents, myReactionEvent } = this.props;

        const classes = classNames({
            mx_ReactionsRowButton: true,
            mx_ReactionsRowButton_selected: !!myReactionEvent,
        });

        let tooltip;
        if (this.state.tooltipRendered) {
            tooltip = <ReactionsRowButtonTooltip
                mxEvent={this.props.mxEvent}
                content={content}
                reactionEvents={reactionEvents}
                visible={this.state.tooltipVisible}
            />;
        }

        const room = this.context.getRoom(mxEvent.getRoomId());
        let label: string;
        if (room) {
            const senders = [];
            for (const reactionEvent of reactionEvents) {
                const member = room.getMember(reactionEvent.getSender());
                senders.push(member?.name || reactionEvent.getSender());
            }

            const reactors = formatCommaSeparatedList(senders, 6);
            if (content) {
                label = _t("%(reactors)s reacted with %(content)s", { reactors, content });
            } else {
                label = reactors;
            }
        }
        const isPeeking = room.getMyMembership() !== "join";
        return <AccessibleButton
            className={classes}
            aria-label={label}
            onClick={this.onClick}
            disabled={isPeeking}
            onMouseOver={this.onMouseOver}
            onMouseLeave={this.onMouseLeave}
        >
            <span className="mx_ReactionsRowButton_content" aria-hidden="true">
                { content }
            </span>
            <span className="mx_ReactionsRowButton_count" aria-hidden="true">
                { count }
            </span>
            { tooltip }
        </AccessibleButton>;
    }
}
