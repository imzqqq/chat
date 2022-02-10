import React, { ComponentProps, useMemo, useState } from 'react';
import { Room } from "matrix-js-sdk/src/models/room";

import ConfirmUserActionDialog from "./ConfirmUserActionDialog";
import SpaceStore from "../../../stores/spaces/SpaceStore";
import SpaceChildrenPicker from "../spaces/SpaceChildrenPicker";

type BaseProps = ComponentProps<typeof ConfirmUserActionDialog>;
interface IProps extends Omit<BaseProps, "groupMember" | "matrixClient" | "children" | "onFinished"> {
    space: Room;
    allLabel: string;
    specificLabel: string;
    noneLabel?: string;
    warningMessage?: string;
    onFinished(success: boolean, reason?: string, rooms?: Room[]): void;
    spaceChildFilter?(child: Room): boolean;
}

const ConfirmSpaceUserActionDialog: React.FC<IProps> = ({
    space,
    spaceChildFilter,
    allLabel,
    specificLabel,
    noneLabel,
    warningMessage,
    onFinished,
    ...props
}) => {
    const spaceChildren = useMemo(() => {
        const children = SpaceStore.instance.getChildren(space.roomId);
        if (spaceChildFilter) {
            return children.filter(spaceChildFilter);
        }
        return children;
    }, [space.roomId, spaceChildFilter]);

    const [roomsToLeave, setRoomsToLeave] = useState<Room[]>([]);
    const selectedRooms = useMemo(() => new Set(roomsToLeave), [roomsToLeave]);

    let warning: JSX.Chat;
    if (warningMessage) {
        warning = <div className="mx_ConfirmSpaceUserActionDialog_warning">
            { warningMessage }
        </div>;
    }

    return (
        <ConfirmUserActionDialog
            {...props}
            onFinished={(success: boolean, reason?: string) => {
                onFinished(success, reason, roomsToLeave);
            }}
            className="mx_ConfirmSpaceUserActionDialog"
        >
            { warning }
            <SpaceChildrenPicker
                space={space}
                spaceChildren={spaceChildren}
                selected={selectedRooms}
                allLabel={allLabel}
                specificLabel={specificLabel}
                noneLabel={noneLabel}
                onChange={setRoomsToLeave}
            />
        </ConfirmUserActionDialog>
    );
};

export default ConfirmSpaceUserActionDialog;

