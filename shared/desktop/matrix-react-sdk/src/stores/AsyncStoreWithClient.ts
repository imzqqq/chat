import { MatrixClient } from "matrix-js-sdk/src/client";
import { AsyncStore } from "./AsyncStore";
import { ActionPayload } from "../dispatcher/payloads";
import { Dispatcher } from "flux";
import { ReadyWatchingStore } from "./ReadyWatchingStore";

export abstract class AsyncStoreWithClient<T extends Object> extends AsyncStore<T> {
    protected readyStore: ReadyWatchingStore;

    protected constructor(dispatcher: Dispatcher<ActionPayload>, initialState: T = <T>{}) {
        super(dispatcher, initialState);

        // Create an anonymous class to avoid code duplication
        const asyncStore = this; // eslint-disable-line @typescript-eslint/no-this-alias
        this.readyStore = new (class extends ReadyWatchingStore {
            public get mxClient(): MatrixClient {
                return this.matrixClient;
            }

            protected async onReady(): Promise<any> {
                return asyncStore.onReady();
            }

            protected async onNotReady(): Promise<any> {
                return asyncStore.onNotReady();
            }
        })(dispatcher);
    }

    get matrixClient(): MatrixClient {
        return this.readyStore.mxClient;
    }

    protected async onReady() {
        // Default implementation is to do nothing.
    }

    protected async onNotReady() {
        // Default implementation is to do nothing.
    }

    protected abstract onAction(payload: ActionPayload): Promise<void>;

    protected async onDispatch(payload: ActionPayload) {
        await this.onAction(payload);
    }
}
