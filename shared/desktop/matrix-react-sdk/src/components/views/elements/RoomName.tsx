import React, { useEffect, useState } from "react";
import { Room } from "matrix-js-sdk/src/models/room";

import { useEventEmitter } from "../../../hooks/useEventEmitter";

interface IProps {
    room: Room;
    children?(name: string): JSX.Chat;
}

const RoomName = ({ room, children }: IProps): JSX.Chat => {
    const [name, setName] = useState(room?.name);
    useEventEmitter(room, "Room.name", () => {
        setName(room?.name);
    });
    useEffect(() => {
        setName(room?.name);
    }, [room]);

    if (children) return children(name);
    return <>{ name || "" }</>;
};

export default RoomName;
