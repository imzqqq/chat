import React from 'react';
import PropTypes from 'prop-types';
import * as sdk from '../../../index';
import dis from '../../../dispatcher/dispatcher';
import { GroupRoomType } from '../../../groups';
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { mediaFromMxc } from "../../../customisations/Media";

@replaceableComponent("views.groups.GroupRoomTile")
class GroupRoomTile extends React.Component {
    static propTypes = {
        groupId: PropTypes.string.isRequired,
        groupRoom: GroupRoomType.isRequired,
    };

    static contextType = MatrixClientContext

    onClick = e => {
        dis.dispatch({
            action: 'view_group_room',
            groupId: this.props.groupId,
            groupRoomId: this.props.groupRoom.roomId,
        });
    };

    render() {
        const BaseAvatar = sdk.getComponent('avatars.BaseAvatar');
        const AccessibleButton = sdk.getComponent('elements.AccessibleButton');
        const avatarUrl = this.props.groupRoom.avatarUrl
            ? mediaFromMxc(this.props.groupRoom.avatarUrl).getSquareThumbnailHttp(36)
            : null;

        const av = (
            <BaseAvatar
                name={this.props.groupRoom.displayname}
                width={36}
                height={36}
                url={avatarUrl}
            />
        );

        return (
            <AccessibleButton className="mx_GroupRoomTile" onClick={this.onClick}>
                <div className="mx_GroupRoomTile_avatar">
                    { av }
                </div>
                <div className="mx_GroupRoomTile_name">
                    { this.props.groupRoom.displayname }
                </div>
            </AccessibleButton>
        );
    }
}

export default GroupRoomTile;
