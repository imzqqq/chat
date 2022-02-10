import { MatrixClient } from "matrix-js-sdk/src/client";
import { MatrixClientPeg } from "../MatrixClientPeg";
import { ActionPayload } from "../dispatcher/payloads";
import { Dispatcher } from "flux";
import { IDestroyable } from "../utils/IDestroyable";
import { EventEmitter } from "events";

export abstract class ReadyWatchingStore extends EventEmitter implements IDestroyable {
    protected matrixClient: MatrixClient;
    private readonly dispatcherRef: string;

    constructor(protected readonly dispatcher: Dispatcher<ActionPayload>) {
        super();

        this.dispatcherRef = this.dispatcher.register(this.onAction);

        if (MatrixClientPeg.get()) {
            this.matrixClient = MatrixClientPeg.get();

            // noinspection JSIgnoredPromiseFromCall
            this.onReady();
        }
    }

    public get mxClient(): MatrixClient {
        return this.matrixClient; // for external readonly access
    }

    public useUnitTestClient(cli: MatrixClient) {
        this.matrixClient = cli;
    }

    public destroy() {
        this.dispatcher.unregister(this.dispatcherRef);
    }

    protected async onReady() {
        // Default implementation is to do nothing.
    }

    protected async onNotReady() {
        // Default implementation is to do nothing.
    }

    private onAction = async (payload: ActionPayload) => {
        if (payload.action === 'MatrixActions.sync') {
            // Only set the client on the transition into the PREPARED state.
            // Everything after this is unnecessary (we only need to know once we have a client)
            // and we intentionally don't set the client before this point to avoid stores
            // updating for every event emitted during the cached sync.
            if (!(payload.prevState === 'PREPARED' && payload.state !== 'PREPARED')) {
                return;
            }

            if (this.matrixClient !== payload.matrixClient) {
                if (this.matrixClient) {
                    await this.onNotReady();
                }
                this.matrixClient = payload.matrixClient;
                await this.onReady();
            }
        } else if (payload.action === 'on_client_not_viable' || payload.action === 'on_logged_out') {
            if (this.matrixClient) {
                await this.onNotReady();
                this.matrixClient = null;
            }
        }
    };
}
