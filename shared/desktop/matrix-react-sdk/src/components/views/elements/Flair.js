import React from 'react';
import PropTypes from 'prop-types';
import FlairStore from '../../../stores/FlairStore';
import dis from '../../../dispatcher/dispatcher';
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { mediaFromMxc } from "../../../customisations/Media";

class FlairAvatar extends React.Component {
    constructor() {
        super();
        this.onClick = this.onClick.bind(this);
    }

    onClick(ev) {
        ev.preventDefault();
        // Don't trigger onClick of parent element
        ev.stopPropagation();
        dis.dispatch({
            action: 'view_group',
            group_id: this.props.groupProfile.groupId,
        });
    }

    render() {
        const httpUrl = mediaFromMxc(this.props.groupProfile.avatarUrl).getSquareThumbnailHttp(16);
        const tooltip = this.props.groupProfile.name ?
            `${this.props.groupProfile.name} (${this.props.groupProfile.groupId})`:
            this.props.groupProfile.groupId;
        return <img
            src={httpUrl}
            width="16"
            height="16"
            onClick={this.onClick}
            title={tooltip} />;
    }
}

FlairAvatar.propTypes = {
    groupProfile: PropTypes.shape({
        groupId: PropTypes.string.isRequired,
        name: PropTypes.string,
        avatarUrl: PropTypes.string.isRequired,
    }),
};

FlairAvatar.contextType = MatrixClientContext;

@replaceableComponent("views.elements.Flair")
export default class Flair extends React.Component {
    constructor() {
        super();
        this.state = {
            profiles: [],
        };
    }

    componentDidMount() {
        this._unmounted = false;
        this._generateAvatars(this.props.groups);
    }

    componentWillUnmount() {
        this._unmounted = true;
    }

    // TODO: [REACT-WARNING] Replace with appropriate lifecycle event
    UNSAFE_componentWillReceiveProps(newProps) {  // eslint-disable-line camelcase
        this._generateAvatars(newProps.groups);
    }

    async _getGroupProfiles(groups) {
        const profiles = [];
        for (const groupId of groups) {
            let groupProfile = null;
            try {
                groupProfile = await FlairStore.getGroupProfileCached(this.context, groupId);
            } catch (err) {
                console.error('Could not get profile for group', groupId, err);
            }
            profiles.push(groupProfile);
        }
        return profiles.filter((p) => p !== null);
    }

    async _generateAvatars(groups) {
        if (!groups || groups.length === 0) {
            return;
        }
        const profiles = await this._getGroupProfiles(groups);
        if (!this.unmounted) {
            this.setState({
                profiles: profiles.filter((profile) => {
                    return profile ? profile.avatarUrl : false;
                }),
            });
        }
    }

    render() {
        if (this.state.profiles.length === 0) {
            return null;
        }
        const avatars = this.state.profiles.map((profile, index) => {
            return <FlairAvatar key={index} groupProfile={profile} />;
        });
        return (
            <span className="mx_Flair">
                { avatars }
            </span>
        );
    }
}

Flair.propTypes = {
    groups: PropTypes.arrayOf(PropTypes.string),
};

Flair.contextType = MatrixClientContext;
