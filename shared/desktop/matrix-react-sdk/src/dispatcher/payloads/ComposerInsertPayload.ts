import { MatrixEvent } from "matrix-js-sdk/src/models/event";

import { ActionPayload } from "../payloads";
import { Action } from "../actions";

interface IBaseComposerInsertPayload extends ActionPayload {
    action: Action.ComposerInsert;
}

interface IComposerInsertMentionPayload extends IBaseComposerInsertPayload {
    userId: string;
}

interface IComposerInsertQuotePayload extends IBaseComposerInsertPayload {
    event: MatrixEvent;
}

interface IComposerInsertPlaintextPayload extends IBaseComposerInsertPayload {
    text: string;
}

export type ComposerInsertPayload =
    IComposerInsertMentionPayload |
    IComposerInsertQuotePayload |
    IComposerInsertPlaintextPayload;

