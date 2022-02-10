import React from 'react';
import { _t } from '../../../languageHandler';
import HeaderButton from './HeaderButton';
import HeaderButtons, { HeaderKind } from './HeaderButtons';
import { RightPanelPhases } from "../../../stores/RightPanelStorePhases";
import { Action } from "../../../dispatcher/actions";
import { ActionPayload } from "../../../dispatcher/payloads";
import { ViewUserPayload } from "../../../dispatcher/payloads/ViewUserPayload";
import { replaceableComponent } from "../../../utils/replaceableComponent";

const GROUP_PHASES = [
    RightPanelPhases.GroupMemberInfo,
    RightPanelPhases.GroupMemberList,
];
const ROOM_PHASES = [
    RightPanelPhases.GroupRoomList,
    RightPanelPhases.GroupRoomInfo,
];

interface IProps {}

@replaceableComponent("views.right_panel.GroupHeaderButtons")
export default class GroupHeaderButtons extends HeaderButtons {
    constructor(props: IProps) {
        super(props, HeaderKind.Group);
    }

    protected onAction(payload: ActionPayload) {
        if (payload.action === Action.ViewUser) {
            if ((payload as ViewUserPayload).member) {
                this.setPhase(RightPanelPhases.RoomMemberInfo, { member: payload.member });
            } else {
                this.setPhase(RightPanelPhases.GroupMemberList);
            }
        } else if (payload.action === "view_group") {
            this.setPhase(RightPanelPhases.GroupMemberList);
        } else if (payload.action === "view_group_room") {
            this.setPhase(
                RightPanelPhases.GroupRoomInfo,
                { groupRoomId: payload.groupRoomId, groupId: payload.groupId },
            );
        } else if (payload.action === "view_group_room_list") {
            this.setPhase(RightPanelPhases.GroupRoomList);
        } else if (payload.action === "view_group_member_list") {
            this.setPhase(RightPanelPhases.GroupMemberList);
        } else if (payload.action === "view_group_user") {
            this.setPhase(RightPanelPhases.GroupMemberInfo, { member: payload.member });
        }
    }

    private onMembersClicked = () => {
        if (this.state.phase === RightPanelPhases.GroupMemberInfo) {
            // send the active phase to trigger a toggle
            this.setPhase(RightPanelPhases.GroupMemberInfo);
        } else {
            // This toggles for us, if needed
            this.setPhase(RightPanelPhases.GroupMemberList);
        }
    };

    private onRoomsClicked = () => {
        // This toggles for us, if needed
        this.setPhase(RightPanelPhases.GroupRoomList);
    };

    renderButtons() {
        return <>
            <HeaderButton
                name="groupMembersButton"
                title={_t('Members')}
                isHighlighted={this.isPhase(GROUP_PHASES)}
                onClick={this.onMembersClicked}
                analytics={['Right Panel', 'Group Member List Button', 'click']}
            />
            <HeaderButton
                name="roomsButton"
                title={_t('Rooms')}
                isHighlighted={this.isPhase(ROOM_PHASES)}
                onClick={this.onRoomsClicked}
                analytics={['Right Panel', 'Group Room List Button', 'click']}
            />
        </>;
    }
}
