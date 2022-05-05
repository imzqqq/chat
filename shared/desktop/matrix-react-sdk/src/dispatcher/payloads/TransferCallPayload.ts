import { ActionPayload } from "../payloads";
import { Action } from "../actions";
import { MatrixCall } from "matrix-js-sdk/src/webrtc/call";

export interface TransferCallPayload extends ActionPayload {
    action: Action.TransferCallToMatrixID | Action.TransferCallToPhoneNumber;
    // The call to transfer
    call: MatrixCall;
    // Where to transfer the call. A Chat ID if action == TransferCallToMatrixID
    // and a phone number if action == TransferCallToPhoneNumber
    destination: string;
    // If true, puts the current call on hold and dials the transfer target, giving
    // the user a button to complete the transfer when ready.
    // If false, ends the call immediately and sends the user to the transfer
    // destination
    consultFirst: boolean;
}
