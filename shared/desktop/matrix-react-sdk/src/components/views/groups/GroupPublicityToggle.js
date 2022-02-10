import React from 'react';
import PropTypes from 'prop-types';
import * as sdk from '../../../index';
import GroupStore from '../../../stores/GroupStore';
import ToggleSwitch from "../elements/ToggleSwitch";
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.groups.GroupPublicityTile")
export default class GroupPublicityToggle extends React.Component {
    static propTypes = {
        groupId: PropTypes.string.isRequired,
    };

    state = {
        busy: false,
        ready: false,
        isGroupPublicised: false, // assume false as <ToggleSwitch /> expects a boolean
    };

    componentDidMount() {
        this._initGroupStore(this.props.groupId);
    }

    _initGroupStore(groupId) {
        this._groupStoreToken = GroupStore.registerListener(groupId, () => {
            this.setState({
                isGroupPublicised: Boolean(GroupStore.getGroupPublicity(groupId)),
                ready: GroupStore.isStateReady(groupId, GroupStore.STATE_KEY.Summary),
            });
        });
    }

    componentWillUnmount() {
        if (this._groupStoreToken) this._groupStoreToken.unregister();
    }

    _onPublicityToggle = () => {
        this.setState({
            busy: true,
            // Optimistic early update
            isGroupPublicised: !this.state.isGroupPublicised,
        });
        GroupStore.setGroupPublicity(this.props.groupId, !this.state.isGroupPublicised).then(() => {
            this.setState({
                busy: false,
            });
        });
    };

    render() {
        const GroupTile = sdk.getComponent('groups.GroupTile');
        return <div className="mx_GroupPublicity_toggle">
            <GroupTile groupId={this.props.groupId} showDescription={false} avatarHeight={40} />
            <ToggleSwitch checked={this.state.isGroupPublicised}
                disabled={!this.state.ready || this.state.busy}
                onChange={this._onPublicityToggle} />
        </div>;
    }
}
