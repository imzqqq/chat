import React from 'react';
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { MsgType } from "matrix-js-sdk/src/@types/event";

import Flair from '../elements/Flair';
import FlairStore from '../../../stores/FlairStore';
import { getUserNameColorClass } from '../../../utils/FormattingUtils';
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { UserNameColorMode } from '../../../settings/UserNameColorMode';

interface IProps {
    mxEvent: MatrixEvent;
    onClick?(): void;
    enableFlair: boolean;
    userNameColorMode?: UserNameColorMode;
}

interface IState {
    userGroups;
    relatedGroups;
}

@replaceableComponent("views.messages.SenderProfile")
export default class SenderProfile extends React.Component<IProps, IState> {
    static contextType = MatrixClientContext;
    private unmounted = false;

    constructor(props: IProps) {
        super(props);
        const senderId = this.props.mxEvent.getSender();

        this.state = {
            userGroups: FlairStore.cachedPublicisedGroups(senderId) || [],
            relatedGroups: [],
        };
    }

    componentDidMount() {
        this.updateRelatedGroups();

        if (this.state.userGroups.length === 0) {
            this.getPublicisedGroups();
        }

        this.context.on('RoomState.events', this.onRoomStateEvents);
    }

    componentWillUnmount() {
        this.unmounted = true;
        this.context.removeListener('RoomState.events', this.onRoomStateEvents);
    }

    private async getPublicisedGroups() {
        const userGroups = await FlairStore.getPublicisedGroupsCached(this.context, this.props.mxEvent.getSender());
        if (this.unmounted) return;
        this.setState({ userGroups });
    }

    private onRoomStateEvents = (event: MatrixEvent) => {
        if (event.getType() === 'm.room.related_groups' && event.getRoomId() === this.props.mxEvent.getRoomId()) {
            this.updateRelatedGroups();
        }
    };

    private updateRelatedGroups() {
        const room = this.context.getRoom(this.props.mxEvent.getRoomId());
        if (!room) return;

        const relatedGroupsEvent = room.currentState.getStateEvents('m.room.related_groups', '');
        this.setState({
            relatedGroups: relatedGroupsEvent?.getContent().groups || [],
        });
    }

    private getDisplayedGroups(userGroups?: string[], relatedGroups?: string[]) {
        let displayedGroups = userGroups || [];
        if (relatedGroups && relatedGroups.length > 0) {
            displayedGroups = relatedGroups.filter((groupId) => {
                return displayedGroups.includes(groupId);
            });
        } else {
            displayedGroups = [];
        }
        return displayedGroups;
    }

    render() {
        const { mxEvent } = this.props;
        const colorClass = getUserNameColorClass(this.props.userNameColorMode,
            mxEvent.getSender(), this.context.getRoom(this.props.mxEvent.getRoomId()));
        const { msgtype } = mxEvent.getContent();

        const disambiguate = mxEvent.sender?.disambiguate;
        const displayName = mxEvent.sender?.rawDisplayName || mxEvent.getSender() || "";
        const mxid = mxEvent.sender?.userId || mxEvent.getSender() || "";

        if (msgtype === MsgType.Emote) {
            return null; // emote message must include the name so don't duplicate it
        }

        let mxidElement;
        if (disambiguate) {
            mxidElement = (
                <span className="mx_SenderProfile_mxid">
                    { mxid }
                </span>
            );
        }

        let flair;
        if (this.props.enableFlair) {
            const displayedGroups = this.getDisplayedGroups(
                this.state.userGroups, this.state.relatedGroups,
            );

            flair = <Flair key='flair'
                userId={mxEvent.getSender()}
                groups={displayedGroups}
            />;
        }

        return (
            <div className="mx_SenderProfile" dir="auto" onClick={this.props.onClick}>
                <span className={`mx_SenderProfile_displayName ${colorClass}`}>
                    { displayName }
                </span>
                { mxidElement }
                { flair }
            </div>
        );
    }
}
