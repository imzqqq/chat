import React from "react";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";

import { unicodeToShortcode } from '../../../HtmlUtils';
import { _t } from '../../../languageHandler';
import { formatCommaSeparatedList } from '../../../utils/FormattingUtils';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import Tooltip from "../elements/Tooltip";
import MatrixClientContext from "../../../contexts/MatrixClientContext";

interface IProps {
    // The event we're displaying reactions for
    mxEvent: MatrixEvent;
    // The reaction content / key / emoji
    content: string;
    // A Set of Chat reaction events for this key
    reactionEvents: Set<MatrixEvent>;
    visible: boolean;
}

@replaceableComponent("views.messages.ReactionsRowButtonTooltip")
export default class ReactionsRowButtonTooltip extends React.PureComponent<IProps> {
    static contextType = MatrixClientContext;

    render() {
        const { content, reactionEvents, mxEvent, visible } = this.props;

        const room = this.context.getRoom(mxEvent.getRoomId());
        let tooltipLabel;
        if (room) {
            const senders = [];
            for (const reactionEvent of reactionEvents) {
                const member = room.getMember(reactionEvent.getSender());
                const name = member ? member.name : reactionEvent.getSender();
                senders.push(name);
            }
            const shortName = unicodeToShortcode(content);
            tooltipLabel = <div>{ _t(
                "<reactors/><reactedWith>reacted with %(shortName)s</reactedWith>",
                {
                    shortName,
                },
                {
                    reactors: () => {
                        return <div className="mx_Tooltip_title">
                            { formatCommaSeparatedList(senders, 6) }
                        </div>;
                    },
                    reactedWith: (sub) => {
                        if (!shortName) {
                            return null;
                        }
                        return <div className="mx_Tooltip_sub">
                            { sub }
                        </div>;
                    },
                },
            ) }</div>;
        }

        let tooltip;
        if (tooltipLabel) {
            tooltip = <Tooltip visible={visible} label={tooltipLabel} />;
        }

        return tooltip;
    }
}
