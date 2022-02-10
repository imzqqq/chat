import React from 'react';
import PropTypes from 'prop-types';
import * as sdk from '../../../index';
import dis from '../../../dispatcher/dispatcher';
import { GroupMemberType } from '../../../groups';
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { mediaFromMxc } from "../../../customisations/Media";

@replaceableComponent("views.groups.GroupMemberTile")
export default class GroupMemberTile extends React.Component {
    static propTypes = {
        groupId: PropTypes.string.isRequired,
        member: GroupMemberType.isRequired,
    };

    static contextType = MatrixClientContext;

    onClick = e => {
        dis.dispatch({
            action: 'view_group_user',
            member: this.props.member,
            groupId: this.props.groupId,
        });
    };

    render() {
        const BaseAvatar = sdk.getComponent('avatars.BaseAvatar');
        const EntityTile = sdk.getComponent('rooms.EntityTile');

        const name = this.props.member.displayname || this.props.member.userId;
        const avatarUrl = this.props.member.avatarUrl
            ? mediaFromMxc(this.props.member.avatarUrl).getSquareThumbnailHttp(36)
            : null;

        const av = (
            <BaseAvatar
                aria-hidden="true"
                name={this.props.member.displayname || this.props.member.userId}
                idName={this.props.member.userId}
                width={36}
                height={36}
                url={avatarUrl}
            />
        );

        return (
            <EntityTile
                name={name}
                avatarJsx={av}
                onClick={this.onClick}
                suppressOnHover={true}
                presenceState="online"
                powerStatus={this.props.member.isPrivileged ? EntityTile.POWER_STATUS_ADMIN : null}
            />
        );
    }
}
