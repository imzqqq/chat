import { Dispatcher } from "flux";
import { Action } from "./actions";
import { ActionPayload, AsyncActionPayload } from "./payloads";

/**
 * A dispatcher for ActionPayloads (the default within the SDK).
 */
export class MatrixDispatcher extends Dispatcher<ActionPayload> {
    /**
     * Dispatches an event on the dispatcher's event bus.
     * @param {ActionPayload} payload Required. The payload to dispatch.
     * @param {boolean=false} sync Optional. Pass true to dispatch
     *        synchronously. This is useful for anything triggering
     *        an operation that the browser requires user interaction
     *        for. Default false (async).
     */
    dispatch<T extends ActionPayload>(payload: T, sync = false) {
        if (payload instanceof AsyncActionPayload) {
            payload.fn((action: ActionPayload) => {
                this.dispatch(action, sync);
            });
            return;
        }

        if (sync) {
            super.dispatch(payload);
        } else {
            // Unless the caller explicitly asked for us to dispatch synchronously,
            // we always set a timeout to do this: The flux dispatcher complains
            // if you dispatch from within a dispatch, so rather than action
            // handlers having to worry about not calling anything that might
            // then dispatch, we just do dispatches asynchronously.
            setTimeout(super.dispatch.bind(this, payload), 0);
        }
    }

    /**
     * Shorthand for dispatch({action: Action.WHATEVER}, sync). No additional
     * properties can be included with this version.
     * @param {Action} action The action to dispatch.
     * @param {boolean=false} sync Whether the dispatch should be sync or not.
     * @see dispatch(action: ActionPayload, sync: boolean)
     */
    fire(action: Action, sync = false) {
        this.dispatch({ action }, sync);
    }
}

export const defaultDispatcher = new MatrixDispatcher();

const anyGlobal = <any>global;
if (!anyGlobal.mxDispatcher) {
    anyGlobal.mxDispatcher = defaultDispatcher;
}

export default defaultDispatcher;
