import { defer, IDeferred } from "matrix-js-sdk/src/utils";

// @ts-ignore - `.ts` is needed here to make TS happy
import BlurhashWorker from "./workers/blurhash.worker.ts";

interface IBlurhashWorkerResponse {
    seq: number;
    blurhash: string;
}

export class BlurhashEncoder {
    private static internalInstance = new BlurhashEncoder();

    public static get instance(): BlurhashEncoder {
        return BlurhashEncoder.internalInstance;
    }

    private readonly worker: Worker;
    private seq = 0;
    private pendingDeferredMap = new Map<number, IDeferred<string>>();

    constructor() {
        this.worker = new BlurhashWorker();
        this.worker.onmessage = this.onMessage;
    }

    private onMessage = (ev: MessageEvent<IBlurhashWorkerResponse>) => {
        const { seq, blurhash } = ev.data;
        const deferred = this.pendingDeferredMap.get(seq);
        if (deferred) {
            this.pendingDeferredMap.delete(seq);
            deferred.resolve(blurhash);
        }
    };

    public getBlurhash(imageData: ImageData): Promise<string> {
        const seq = this.seq++;
        const deferred = defer<string>();
        this.pendingDeferredMap.set(seq, deferred);
        this.worker.postMessage({ seq, imageData });
        return deferred.promise;
    }
}

