import "../skinned-sdk"; // Must be first for skinning to work
import EditorModel from "../../src/editor/model";
import { createPartCreator } from "./mock";

function createRenderer() {
    const render = (c) => {
        render.caret = c;
        render.count += 1;
    };
    render.count = 0;
    render.caret = null;
    return render;
}

describe('editor/position', function() {
    it('move first position backward in empty model', function() {
        const model = new EditorModel([], createPartCreator(), createRenderer());
        const pos = model.positionForOffset(0, true);
        const pos2 = pos.backwardsWhile(model, () => true);
        expect(pos).toBe(pos2);
    });
    it('move first position forwards in empty model', function() {
        const model = new EditorModel([], createPartCreator(), createRenderer());
        const pos = model.positionForOffset(0, true);
        const pos2 = pos.forwardsWhile(() => true);
        expect(pos).toBe(pos2);
    });
    it('move forwards within one part', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.plain("hello")], pc, createRenderer());
        const pos = model.positionForOffset(1);
        let n = 3;
        const pos2 = pos.forwardsWhile(model, () => { n -= 1; return n >= 0; });
        expect(pos2.index).toBe(0);
        expect(pos2.offset).toBe(4);
    });
    it('move forwards crossing to other part', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.plain("hello"), pc.plain(" world")], pc, createRenderer());
        const pos = model.positionForOffset(4);
        let n = 3;
        const pos2 = pos.forwardsWhile(model, () => { n -= 1; return n >= 0; });
        expect(pos2.index).toBe(1);
        expect(pos2.offset).toBe(2);
    });
    it('move backwards within one part', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.plain("hello")], pc, createRenderer());
        const pos = model.positionForOffset(4);
        let n = 3;
        const pos2 = pos.backwardsWhile(model, () => { n -= 1; return n >= 0; });
        expect(pos2.index).toBe(0);
        expect(pos2.offset).toBe(1);
    });
    it('move backwards crossing to other part', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.plain("hello"), pc.plain(" world")], pc, createRenderer());
        const pos = model.positionForOffset(7);
        let n = 3;
        const pos2 = pos.backwardsWhile(model, () => { n -= 1; return n >= 0; });
        expect(pos2.index).toBe(0);
        expect(pos2.offset).toBe(4);
    });
});
