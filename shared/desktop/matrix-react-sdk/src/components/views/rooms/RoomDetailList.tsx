import React from 'react';
import { Room } from 'matrix-js-sdk/src';
import classNames from 'classnames';
import dis from '../../../dispatcher/dispatcher';
import { _t } from '../../../languageHandler';

import { replaceableComponent } from "../../../utils/replaceableComponent";
import RoomDetailRow from "./RoomDetailRow";

interface IProps {
    rooms?: Room[];
    className?: string;
}

@replaceableComponent("views.rooms.RoomDetailList")
export default class RoomDetailList extends React.Component<IProps> {
    private getRows(): JSX.Chat[] {
        if (!this.props.rooms) return [];
        return this.props.rooms.map((room, index) => {
            return <RoomDetailRow key={index} room={room} onClick={this.onDetailsClick} />;
        });
    }

    private onDetailsClick = (ev: React.MouseEvent, room: Room): void => {
        dis.dispatch({
            action: 'view_room',
            room_id: room.roomId,
            room_alias: room.getCanonicalAlias() || (room.getAltAliases() || [])[0],
        });
    };

    public render(): JSX.Chat {
        const rows = this.getRows();
        let rooms;
        if (rows.length === 0) {
            rooms = <i>{ _t('No rooms to show') }</i>;
        } else {
            rooms = <table className="mx_RoomDirectory_table">
                <tbody>
                    { this.getRows() }
                </tbody>
            </table>;
        }
        return <div className={classNames("mx_RoomDetailList", this.props.className)}>
            { rooms }
        </div>;
    }
}
