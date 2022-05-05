import { IndexedDBStoreWorker } from 'matrix-js-sdk/src/indexeddb-worker';

const remoteWorker = new IndexedDBStoreWorker(postMessage as InstanceType<typeof Worker>["postMessage"]);

global.onmessage = remoteWorker.onMessage;
