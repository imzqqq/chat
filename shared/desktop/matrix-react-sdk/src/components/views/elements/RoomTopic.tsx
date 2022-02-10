import React, { useEffect, useState } from "react";
import { EventType } from "matrix-js-sdk/src/@types/event";
import { Room } from "matrix-js-sdk/src/models/room";

import { useEventEmitter } from "../../../hooks/useEventEmitter";
import { linkifyElement } from "../../../HtmlUtils";

interface IProps {
    room?: Room;
    children?(topic: string, ref: (element: HTMLElement) => void): JSX.Chat;
}

export const getTopic = room => room?.currentState?.getStateEvents(EventType.RoomTopic, "")?.getContent()?.topic;

const RoomTopic = ({ room, children }: IProps): JSX.Chat => {
    const [topic, setTopic] = useState(getTopic(room));
    useEventEmitter(room.currentState, "RoomState.events", () => {
        setTopic(getTopic(room));
    });
    useEffect(() => {
        setTopic(getTopic(room));
    }, [room]);

    const ref = e => e && linkifyElement(e);
    if (children) return children(topic, ref);
    return <span ref={ref}>{ topic }</span>;
};

export default RoomTopic;
