import "../skinned-sdk"; // Must be first for skinning to work
import EditorModel from "../../src/editor/model";
import { htmlSerializeIfNeeded } from "../../src/editor/serialize";
import { createPartCreator } from "./mock";

describe('editor/serialize', function() {
    it('user pill turns message into html', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.userPill("Alice", "@alice:hs.tld")]);
        const html = htmlSerializeIfNeeded(model, {});
        expect(html).toBe("<a href=\"https://to.chat.dingshunyu.top/#/@alice:hs.tld\">Alice</a>");
    });
    it('room pill turns message into html', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.roomPill("#room:hs.tld")]);
        const html = htmlSerializeIfNeeded(model, {});
        expect(html).toBe("<a href=\"https://to.chat.dingshunyu.top/#/#room:hs.tld\">#room:hs.tld</a>");
    });
    it('@room pill turns message into html', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.atRoomPill("@room")]);
        const html = htmlSerializeIfNeeded(model, {});
        expect(html).toBeFalsy();
    });
    it('any markdown turns message into html', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.plain("*hello* world")]);
        const html = htmlSerializeIfNeeded(model, {});
        expect(html).toBe("<em>hello</em> world");
    });
    it('displaynames ending in a backslash work', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.userPill("Displayname\\", "@user:server")]);
        const html = htmlSerializeIfNeeded(model, {});
        expect(html).toBe("<a href=\"https://to.chat.dingshunyu.top/#/@user:server\">Displayname\\</a>");
    });
    it('displaynames containing an opening square bracket work', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.userPill("Displayname[[", "@user:server")]);
        const html = htmlSerializeIfNeeded(model, {});
        expect(html).toBe("<a href=\"https://to.chat.dingshunyu.top/#/@user:server\">Displayname[[</a>");
    });
    it('displaynames containing a closing square bracket work', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.userPill("Displayname]", "@user:server")]);
        const html = htmlSerializeIfNeeded(model, {});
        expect(html).toBe("<a href=\"https://to.chat.dingshunyu.top/#/@user:server\">Displayname]</a>");
    });
    it('escaped markdown should not retain backslashes', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.plain('\\*hello\\* world')]);
        const html = htmlSerializeIfNeeded(model, {});
        expect(html).toBe('*hello* world');
    });
    it('escaped markdown should convert HTML entities', function() {
        const pc = createPartCreator();
        const model = new EditorModel([pc.plain('\\*hello\\* world < hey world!')]);
        const html = htmlSerializeIfNeeded(model, {});
        expect(html).toBe('*hello* world &lt; hey world!');
    });
});
