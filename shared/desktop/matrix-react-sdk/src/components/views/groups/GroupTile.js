import React from 'react';
import PropTypes from 'prop-types';
import * as sdk from '../../../index';
import dis from '../../../dispatcher/dispatcher';
import FlairStore from '../../../stores/FlairStore';
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { mediaFromMxc } from "../../../customisations/Media";
import { _t } from "../../../languageHandler";
import TagOrderActions from "../../../actions/TagOrderActions";
import GroupFilterOrderStore from "../../../stores/GroupFilterOrderStore";

@replaceableComponent("views.groups.GroupTile")
class GroupTile extends React.Component {
    static propTypes = {
        groupId: PropTypes.string.isRequired,
        // Whether to show the short description of the group on the tile
        showDescription: PropTypes.bool,
        // Height of the group avatar in pixels
        avatarHeight: PropTypes.number,
    };

    static contextType = MatrixClientContext;

    static defaultProps = {
        showDescription: true,
        avatarHeight: 50,
    };

    state = {
        profile: null,
    };

    componentDidMount() {
        FlairStore.getGroupProfileCached(this.context, this.props.groupId).then((profile) => {
            this.setState({ profile });
        }).catch((err) => {
            console.error('Error whilst getting cached profile for GroupTile', err);
        });
    }

    onClick = e => {
        e.preventDefault();
        dis.dispatch({
            action: 'view_group',
            group_id: this.props.groupId,
        });
    };

    onPinClick = e => {
        e.preventDefault();
        e.stopPropagation();
        dis.dispatch(TagOrderActions.moveTag(this.context, this.props.groupId, 0));
    };

    onUnpinClick = e => {
        e.preventDefault();
        e.stopPropagation();
        dis.dispatch(TagOrderActions.removeTag(this.context, this.props.groupId));
    };

    render() {
        const BaseAvatar = sdk.getComponent('avatars.BaseAvatar');
        const AccessibleButton = sdk.getComponent('elements.AccessibleButton');
        const profile = this.state.profile || {};
        const name = profile.name || this.props.groupId;
        const avatarHeight = this.props.avatarHeight;
        const descElement = this.props.showDescription ?
            <div className="mx_GroupTile_desc">{ profile.shortDescription }</div> :
            <div />;
        const httpUrl = profile.avatarUrl
            ? mediaFromMxc(profile.avatarUrl).getSquareThumbnailHttp(avatarHeight)
            : null;

        const avatarElement = (
            <div className="mx_GroupTile_avatar">
                <BaseAvatar
                    name={name}
                    idName={this.props.groupId}
                    url={httpUrl}
                    width={avatarHeight}
                    height={avatarHeight} />
            </div>
        );

        return <AccessibleButton className="mx_GroupTile" onClick={this.onClick}>
            { avatarElement }
            <div className="mx_GroupTile_profile">
                <div className="mx_GroupTile_name">{ name }</div>
                { descElement }
                <div className="mx_GroupTile_groupId">{ this.props.groupId }</div>
                { !(GroupFilterOrderStore.getOrderedTags() || []).includes(this.props.groupId)
                    ? <AccessibleButton kind="link" onClick={this.onPinClick}>
                        { _t("Pin") }
                    </AccessibleButton>
                    : <AccessibleButton kind="link" onClick={this.onUnpinClick}>
                        { _t("Unpin") }
                    </AccessibleButton>
                }
            </div>
        </AccessibleButton>;
    }
}

export default GroupTile;
