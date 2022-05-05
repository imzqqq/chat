import React from "react";
import { Room } from "matrix-js-sdk/src/models/room";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";

import dis from "../../../dispatcher/dispatcher";
import AccessibleButton from "../elements/AccessibleButton";
import MessageEvent from "../messages/MessageEvent";
import MemberAvatar from "../avatars/MemberAvatar";
import { _t } from '../../../languageHandler';
import { formatDate } from '../../../DateUtils';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import MatrixClientContext from "../../../contexts/MatrixClientContext";
import { getUserNameColorClass } from "../../../utils/FormattingUtils";
import AccessibleTooltipButton from "../elements/AccessibleTooltipButton";
import { TileShape } from "./EventTile";
import { UserNameColorMode } from "../../../settings/UserNameColorMode";

interface IProps {
    room: Room;
    event: MatrixEvent;
    onUnpinClicked?(): void;
    userNameColorMode?: UserNameColorMode;
}

const AVATAR_SIZE = 24;

@replaceableComponent("views.rooms.PinnedEventTile")
export default class PinnedEventTile extends React.Component<IProps> {
    public static contextType = MatrixClientContext;

    private onTileClicked = () => {
        dis.dispatch({
            action: 'view_room',
            event_id: this.props.event.getId(),
            highlighted: true,
            room_id: this.props.event.getRoomId(),
        });
    };

    render() {
        const sender = this.props.event.getSender();
        const senderProfile = this.props.room.getMember(sender);

        let unpinButton = null;
        if (this.props.onUnpinClicked) {
            unpinButton = (
                <AccessibleTooltipButton
                    onClick={this.props.onUnpinClicked}
                    className="mx_PinnedEventTile_unpinButton"
                    title={_t("Unpin")}
                />
            );
        }

        return <div className="mx_PinnedEventTile">
            <MemberAvatar
                className="mx_PinnedEventTile_senderAvatar"
                member={senderProfile}
                width={AVATAR_SIZE}
                height={AVATAR_SIZE}
                fallbackUserId={sender}
            />

            <span className={"mx_PinnedEventTile_sender " + getUserNameColorClass(this.props.userNameColorMode, sender, this.props.room)}>
                { senderProfile?.name || sender }
            </span>

            { unpinButton }

            <div className="mx_PinnedEventTile_message">
                <MessageEvent
                    mxEvent={this.props.event}
                    // @ts-ignore - complaining that className is invalid when it's not
                    className="mx_PinnedEventTile_body"
                    maxImageHeight={150}
                    onHeightChanged={() => {}} // we need to give this, apparently
                    tileShape={TileShape.Pinned}
                />
            </div>

            <div className="mx_PinnedEventTile_footer">
                <span className="mx_PinnedEventTile_timestamp">
                    { formatDate(new Date(this.props.event.getTs())) }
                </span>

                <AccessibleButton onClick={this.onTileClicked} kind="link">
                    { _t("View message") }
                </AccessibleButton>
            </div>
        </div>;
    }
}
