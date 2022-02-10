import { EventEmitter } from "events";
import { ReEmitter } from "../../src/ReEmitter";

const EVENTNAME = "UnknownEntry";

class EventSource extends EventEmitter {
    doTheThing() {
        this.emit(EVENTNAME, "foo", "bar");
    }

    doAnError() {
        this.emit('error');
    }
}

class EventTarget extends EventEmitter {

}

describe("ReEmitter", function() {
    it("Re-Emits events with the same args", function() {
        const src = new EventSource();
        const tgt = new EventTarget();

        const handler = jest.fn();
        tgt.on(EVENTNAME, handler);

        const reEmitter = new ReEmitter(tgt);
        reEmitter.reEmit(src, [EVENTNAME]);

        src.doTheThing();

        // Args should be the args passed to 'emit' after the event name, and
        // also the source object of the event which re-emitter adds
        expect(handler).toHaveBeenCalledWith("foo", "bar", src);
    });

    it("Doesn't throw if no handler for 'error' event", function() {
        const src = new EventSource();
        const tgt = new EventTarget();

        const reEmitter = new ReEmitter(tgt);
        reEmitter.reEmit(src, ['error']);

        // without the workaround in ReEmitter, this would throw
        src.doAnError();

        const handler = jest.fn();
        tgt.on('error', handler);

        src.doAnError();

        // Now we've attached an error handler, it should be called
        expect(handler).toHaveBeenCalled();
    });
});
