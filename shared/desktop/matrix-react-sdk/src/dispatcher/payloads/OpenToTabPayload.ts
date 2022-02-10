import { ActionPayload } from "../payloads";
import { Action } from "../actions";

export interface OpenToTabPayload extends ActionPayload {
    action: Action.ViewUserSettings | string; // TODO: Add room settings action

    /**
     * The tab ID to open in the settings view to start, if possible.
     */
    initialTabId?: string;
}
