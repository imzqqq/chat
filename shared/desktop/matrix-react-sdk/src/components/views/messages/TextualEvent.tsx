import React from "react";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";

import RoomContext from "../../../contexts/RoomContext";
import * as TextForEvent from "../../../TextForEvent";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface IProps {
    mxEvent: MatrixEvent;
}

@replaceableComponent("views.messages.TextualEvent")
export default class TextualEvent extends React.Component<IProps> {
    static contextType = RoomContext;

    public render() {
        const text = TextForEvent.textForEvent(this.props.mxEvent, true, this.context?.showHiddenEventsInTimeline);
        if (!text) return null;
        return <div className="mx_TextualEvent">{ text }</div>;
    }
}
