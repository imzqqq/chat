import defaultDispatcher from "../dispatcher/dispatcher";
import { AsyncStore } from "./AsyncStore";
import { ActionPayload } from "../dispatcher/payloads";

interface IState {
    hostSignupActive?: boolean;
}

export class HostSignupStore extends AsyncStore<IState> {
    private static internalInstance = new HostSignupStore();

    private constructor() {
        super(defaultDispatcher, { hostSignupActive: false });
    }

    public static get instance(): HostSignupStore {
        return HostSignupStore.internalInstance;
    }

    public get isHostSignupActive(): boolean {
        return this.state.hostSignupActive;
    }

    public async setHostSignupActive(status: boolean) {
        return this.updateState({
            hostSignupActive: status,
        });
    }

    protected onDispatch(payload: ActionPayload) {
        // Nothing to do
    }
}
