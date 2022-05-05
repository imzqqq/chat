import { ActionPayload } from "../payloads";
import { Action } from "../actions";

export interface DialNumberPayload extends ActionPayload {
    action: Action.DialNumber;
    number: string;
}
