import { Store } from 'flux/utils';

import dis from '../dispatcher/dispatcher';
import { ActionPayload } from "../dispatcher/payloads";

interface IState {
    deferredAction: any;
}

const INITIAL_STATE = {
    deferredAction: null,
};

/**
 * A class for storing application state to do with authentication. This is a simple flux
 * store that listens for actions and updates its state accordingly, informing any
 * listeners (views) of state changes.
 */
class LifecycleStore extends Store<ActionPayload> {
    private state: IState = INITIAL_STATE;

    constructor() {
        super(dis);
    }

    private setState(newState: Partial<IState>) {
        this.state = Object.assign(this.state, newState);
        this.__emitChange();
    }

    protected __onDispatch(payload: ActionPayload) { // eslint-disable-line @typescript-eslint/naming-convention
        switch (payload.action) {
            case 'do_after_sync_prepared':
                this.setState({
                    deferredAction: payload.deferred_action,
                });
                break;
            case 'cancel_after_sync_prepared':
                this.setState({
                    deferredAction: null,
                });
                break;
            case 'sync_state': {
                if (payload.state !== 'PREPARED') {
                    break;
                }
                if (!this.state.deferredAction) break;
                const deferredAction = Object.assign({}, this.state.deferredAction);
                this.setState({
                    deferredAction: null,
                });
                dis.dispatch(deferredAction);
                break;
            }
            case 'on_client_not_viable':
            case 'on_logged_out':
                this.reset();
                break;
        }
    }

    private reset() {
        this.state = Object.assign({}, INITIAL_STATE);
    }
}

let singletonLifecycleStore = null;
if (!singletonLifecycleStore) {
    singletonLifecycleStore = new LifecycleStore();
}
export default singletonLifecycleStore;
