import React from 'react';
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { _t } from "../../../languageHandler";
import WidgetStore from "../../../stores/WidgetStore";
import EventTileBubble from "./EventTileBubble";
import { MatrixClientPeg } from "../../../MatrixClientPeg";
import { Container, WidgetLayoutStore } from "../../../stores/widgets/WidgetLayoutStore";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    mxEvent: MatrixEvent;
}

@replaceableComponent("views.messages.MJitsiWidgetEvent")
export default class MJitsiWidgetEvent extends React.PureComponent<IProps> {
    constructor(props) {
        super(props);
    }

    render() {
        const url = this.props.mxEvent.getContent()['url'];
        const prevUrl = this.props.mxEvent.getPrevContent()['url'];
        const senderName = this.props.mxEvent.sender?.name || this.props.mxEvent.getSender();
        const room = MatrixClientPeg.get().getRoom(this.props.mxEvent.getRoomId());
        const widgetId = this.props.mxEvent.getStateKey();
        const widget = WidgetStore.instance.getRoom(room.roomId, true).widgets.find(w => w.id === widgetId);

        let joinCopy = _t('Join the conference at the top of this room');
        if (widget && WidgetLayoutStore.instance.isInContainer(room, widget, Container.Right)) {
            joinCopy = _t('Join the conference from the room information card on the right');
        } else if (!widget) {
            joinCopy = null;
        }

        if (!url) {
            // removed
            return <EventTileBubble
                className="mx_MJitsiWidgetEvent"
                title={_t('Video conference ended by %(senderName)s', { senderName })}
            />;
        } else if (prevUrl) {
            // modified
            return <EventTileBubble
                className="mx_MJitsiWidgetEvent"
                title={_t('Video conference updated by %(senderName)s', { senderName })}
                subtitle={joinCopy}
            />;
        } else {
            // assume added
            return <EventTileBubble
                className="mx_MJitsiWidgetEvent"
                title={_t("Video conference started by %(senderName)s", { senderName })}
                subtitle={joinCopy}
            />;
        }
    }
}
