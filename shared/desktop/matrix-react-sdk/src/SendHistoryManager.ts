import { clamp } from "lodash";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";

import { SerializedPart } from "./editor/parts";
import EditorModel from "./editor/model";

interface IHistoryItem {
    parts: SerializedPart[];
    replyEventId?: string;
}

export default class SendHistoryManager {
    history: Array<IHistoryItem> = [];
    prefix: string;
    lastIndex = 0; // used for indexing the storage
    currentIndex = 0; // used for indexing the loaded validated history Array

    constructor(roomId: string, prefix: string) {
        this.prefix = prefix + roomId;

        // TODO: Performance issues?
        let index = 0;
        let itemJSON;

        while (itemJSON = sessionStorage.getItem(`${this.prefix}[${index}]`)) {
            try {
                this.history.push(JSON.parse(itemJSON));
            } catch (e) {
                console.warn("Throwing away unserialisable history", e);
                break;
            }
            ++index;
        }
        this.lastIndex = this.history.length - 1;
        // reset currentIndex to account for any unserialisable history
        this.currentIndex = this.lastIndex + 1;
    }

    static createItem(model: EditorModel, replyEvent?: MatrixEvent): IHistoryItem {
        return {
            parts: model.serializeParts(),
            replyEventId: replyEvent ? replyEvent.getId() : undefined,
        };
    }

    save(editorModel: EditorModel, replyEvent?: MatrixEvent) {
        const item = SendHistoryManager.createItem(editorModel, replyEvent);
        this.history.push(item);
        this.currentIndex = this.history.length;
        this.lastIndex += 1;
        sessionStorage.setItem(`${this.prefix}[${this.lastIndex}]`, JSON.stringify(item));
    }

    getItem(offset: number): IHistoryItem {
        this.currentIndex = clamp(this.currentIndex + offset, 0, this.history.length - 1);
        return this.history[this.currentIndex];
    }
}
