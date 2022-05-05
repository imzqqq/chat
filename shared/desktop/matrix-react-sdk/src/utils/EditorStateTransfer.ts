import { MatrixEvent } from "matrix-js-sdk/src/models/event";

import { SerializedPart } from "../editor/parts";
import DocumentOffset from "../editor/offset";

/**
 * Used while editing, to pass the event, and to preserve editor state
 * from one editor instance to another when remounting the editor
 * upon receiving the remote echo for an unsent event.
 */
export default class EditorStateTransfer {
    private serializedParts: SerializedPart[] = null;
    private caret: DocumentOffset = null;

    constructor(private readonly event: MatrixEvent) {}

    public setEditorState(caret: DocumentOffset, serializedParts: SerializedPart[]) {
        this.caret = caret;
        this.serializedParts = serializedParts;
    }

    public hasEditorState(): boolean {
        return !!this.serializedParts;
    }

    public getSerializedParts(): SerializedPart[] {
        return this.serializedParts;
    }

    public getCaret(): DocumentOffset {
        return this.caret;
    }

    public getEvent(): MatrixEvent {
        return this.event;
    }
}
