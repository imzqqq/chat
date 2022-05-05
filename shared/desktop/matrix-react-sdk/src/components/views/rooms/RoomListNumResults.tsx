import React, { useEffect, useState } from "react";

import { _t } from "../../../languageHandler";
import RoomListStore, { LISTS_UPDATE_EVENT } from "../../../stores/room-list/RoomListStore";
import { useEventEmitter } from "../../../hooks/useEventEmitter";
import SpaceStore from "../../../stores/spaces/SpaceStore";

interface IProps {
    onVisibilityChange?: () => void;
}

const RoomListNumResults: React.FC<IProps> = ({ onVisibilityChange }) => {
    const [count, setCount] = useState<number>(null);
    useEventEmitter(RoomListStore.instance, LISTS_UPDATE_EVENT, () => {
        if (RoomListStore.instance.getFirstNameFilterCondition()) {
            const numRooms = Object.values(RoomListStore.instance.orderedLists).flat(1).length;
            setCount(numRooms);
        } else {
            setCount(null);
        }
    });

    useEffect(() => {
        if (onVisibilityChange) {
            onVisibilityChange();
        }
    }, [count, onVisibilityChange]);

    if (typeof count !== "number") return null;

    return <div className="mx_LeftPanel_roomListFilterCount">
        { SpaceStore.instance.spacePanelSpaces.length
            ? _t("%(count)s results in all spaces", { count })
            : _t("%(count)s results", { count })
        }
    </div>;
};

export default RoomListNumResults;
