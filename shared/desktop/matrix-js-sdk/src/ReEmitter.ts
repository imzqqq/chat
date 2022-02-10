import { EventEmitter } from "events";

export class ReEmitter {
    private target: EventEmitter;

    constructor(target: EventEmitter) {
        this.target = target;
    }

    reEmit(source: EventEmitter, eventNames: string[]) {
        for (const eventName of eventNames) {
            // We include the source as the last argument for event handlers which may need it,
            // such as read receipt listeners on the client class which won't have the context
            // of the room.
            const forSource = (...args) => {
                // EventEmitter special cases 'error' to make the emit function throw if no
                // handler is attached, which sort of makes sense for making sure that something
                // handles an error, but for re-emitting, there could be a listener on the original
                // source object so the test doesn't really work. We *could* try to replicate the
                // same logic and throw if there is no listener on either the source or the target,
                // but this behaviour is fairly undesireable for us anyway: the main place we throw
                // 'error' events is for calls, where error events are usually emitted some time
                // later by a different part of the code where 'emit' throwing because the app hasn't
                // added an error handler isn't terribly helpful. (A better fix in retrospect may
                // have been to just avoid using the event name 'error', but backwards compat...)
                if (eventName === 'error' && this.target.listenerCount('error') === 0) return;
                this.target.emit(eventName, ...args, source);
            };
            source.on(eventName, forSource);
        }
    }
}
