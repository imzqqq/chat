import { VerificationRequest } from "matrix-js-sdk/src/crypto/verification/request/VerificationRequest";
import { Room } from "matrix-js-sdk/src/models/room";
import { RoomMember } from "matrix-js-sdk/src/models/room-member";
import { User } from "matrix-js-sdk/src/models/user";
import { RightPanelPhases } from "../../stores/RightPanelStorePhases";
import { ActionPayload } from "../payloads";
import { Action } from "../actions";

export interface SetRightPanelPhasePayload extends ActionPayload {
    action: Action.SetRightPanelPhase;

    phase: RightPanelPhases;
    refireParams?: SetRightPanelPhaseRefireParams;

    /**
     * By default SetRightPanelPhase can close the panel, this allows overriding that behaviour
     */
    allowClose?: boolean;
}

export interface SetRightPanelPhaseRefireParams {
    member?: RoomMember | User;
    verificationRequest?: VerificationRequest;
    groupId?: string;
    groupRoomId?: string;
    // XXX: The type for event should 'view_3pid_invite' action's payload
    event?: any;
    widgetId?: string;
    space?: Room;
}
