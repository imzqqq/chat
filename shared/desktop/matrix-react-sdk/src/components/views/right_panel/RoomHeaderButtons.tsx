import React from "react";
import { Room } from "matrix-js-sdk/src/models/room";

import { _t } from '../../../languageHandler';
import HeaderButton from './HeaderButton';
import HeaderButtons, { HeaderKind } from './HeaderButtons';
import { RightPanelPhases } from "../../../stores/RightPanelStorePhases";
import { Action } from "../../../dispatcher/actions";
import { ActionPayload } from "../../../dispatcher/payloads";
import RightPanelStore from "../../../stores/RightPanelStore";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { useSettingValue } from "../../../hooks/useSettings";
import { useReadPinnedEvents, usePinnedEvents } from './PinnedMessagesCard';

const ROOM_INFO_PHASES = [
    RightPanelPhases.RoomSummary,
    RightPanelPhases.Widget,
    RightPanelPhases.FilePanel,
    RightPanelPhases.RoomMemberList,
    RightPanelPhases.RoomMemberInfo,
    RightPanelPhases.EncryptionPanel,
    RightPanelPhases.Room3pidMemberInfo,
];

const PinnedMessagesHeaderButton = ({ room, isHighlighted, onClick }) => {
    const pinningEnabled = useSettingValue("feature_pinning");
    const pinnedEvents = usePinnedEvents(pinningEnabled && room);
    const readPinnedEvents = useReadPinnedEvents(pinningEnabled && room);
    if (!pinningEnabled) return null;

    let unreadIndicator;
    if (pinnedEvents.some(id => !readPinnedEvents.has(id))) {
        unreadIndicator = <div className="mx_RightPanel_pinnedMessagesButton_unreadIndicator" />;
    }

    return <HeaderButton
        name="pinnedMessagesButton"
        title={_t("Pinned messages")}
        isHighlighted={isHighlighted}
        onClick={onClick}
        analytics={["Right Panel", "Pinned Messages Button", "click"]}
    >
        { unreadIndicator }
    </HeaderButton>;
};

interface IProps {
    room?: Room;
}

@replaceableComponent("views.right_panel.RoomHeaderButtons")
export default class RoomHeaderButtons extends HeaderButtons<IProps> {
    constructor(props: IProps) {
        super(props, HeaderKind.Room);
    }

    protected onAction(payload: ActionPayload) {
        if (payload.action === Action.ViewUser) {
            if (payload.member) {
                this.setPhase(RightPanelPhases.RoomMemberInfo, { member: payload.member });
            } else {
                this.setPhase(RightPanelPhases.RoomMemberList);
            }
        } else if (payload.action === "view_3pid_invite") {
            if (payload.event) {
                this.setPhase(RightPanelPhases.Room3pidMemberInfo, { event: payload.event });
            } else {
                this.setPhase(RightPanelPhases.RoomMemberList);
            }
        }
    }

    private onRoomSummaryClicked = () => {
        // use roomPanelPhase rather than this.state.phase as it remembers the latest one if we close
        const lastPhase = RightPanelStore.getSharedInstance().roomPanelPhase;
        if (ROOM_INFO_PHASES.includes(lastPhase)) {
            if (this.state.phase === lastPhase) {
                this.setPhase(lastPhase);
            } else {
                this.setPhase(lastPhase, RightPanelStore.getSharedInstance().roomPanelPhaseParams);
            }
        } else {
            // This toggles for us, if needed
            this.setPhase(RightPanelPhases.RoomSummary);
        }
    };

    private onNotificationsClicked = () => {
        // This toggles for us, if needed
        this.setPhase(RightPanelPhases.NotificationPanel);
    };

    private onPinnedMessagesClicked = () => {
        // This toggles for us, if needed
        this.setPhase(RightPanelPhases.PinnedMessages);
    };

    public renderButtons() {
        return <>
            <PinnedMessagesHeaderButton
                room={this.props.room}
                isHighlighted={this.isPhase(RightPanelPhases.PinnedMessages)}
                onClick={this.onPinnedMessagesClicked}
            />
            <HeaderButton
                name="notifsButton"
                title={_t('Notifications')}
                isHighlighted={this.isPhase(RightPanelPhases.NotificationPanel)}
                onClick={this.onNotificationsClicked}
                analytics={['Right Panel', 'Notification List Button', 'click']}
            />
            <HeaderButton
                name="roomSummaryButton"
                title={_t('Room Info')}
                isHighlighted={this.isPhase(ROOM_INFO_PHASES)}
                onClick={this.onRoomSummaryClicked}
                analytics={['Right Panel', 'Room Summary Button', 'click']}
            />
        </>;
    }
}
