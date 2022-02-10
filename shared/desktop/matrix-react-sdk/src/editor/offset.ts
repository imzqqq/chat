import EditorModel from "./model";
import DocumentPosition from "./position";

export default class DocumentOffset {
    constructor(public offset: number, public readonly atNodeEnd: boolean) {
    }

    public asPosition(model: EditorModel): DocumentPosition {
        return model.positionForOffset(this.offset, this.atNodeEnd);
    }

    public add(delta: number, atNodeEnd = false): DocumentOffset {
        return new DocumentOffset(this.offset + delta, atNodeEnd);
    }
}
