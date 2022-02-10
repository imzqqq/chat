// @ts-ignore - `.ts` is needed here to make TS happy
import IndexedDBWorker from "../workers/indexeddb.worker.ts";
import { createClient, ICreateClientOpts } from "matrix-js-sdk/src/matrix";
import { IndexedDBCryptoStore } from "matrix-js-sdk/src/crypto/store/indexeddb-crypto-store";
import { WebStorageSessionStore } from "matrix-js-sdk/src/store/session/webstorage";
import { IndexedDBStore } from "matrix-js-sdk/src/store/indexeddb";

const localStorage = window.localStorage;

// just *accessing* indexedDB throws an exception in firefox with
// indexeddb disabled.
let indexedDB;
try {
    indexedDB = window.indexedDB;
} catch (e) {}

/**
 * Create a new matrix client, with the persistent stores set up appropriately
 * (using localstorage/indexeddb, etc)
 *
 * @param {Object} opts  options to pass to Chat.createClient. This will be
 *    extended with `sessionStore` and `store` members.
 *
 * @returns {MatrixClient} the newly-created MatrixClient
 */
export default function createMatrixClient(opts: ICreateClientOpts) {
    const storeOpts: Partial<ICreateClientOpts> = {
        useAuthorizationHeader: true,
    };

    if (indexedDB && localStorage) {
        storeOpts.store = new IndexedDBStore({
            indexedDB: indexedDB,
            dbName: "riot-web-sync",
            localStorage: localStorage,
            workerFactory: () => new IndexedDBWorker(),
        });
    }

    if (localStorage) {
        storeOpts.sessionStore = new WebStorageSessionStore(localStorage);
    }

    if (indexedDB) {
        storeOpts.cryptoStore = new IndexedDBCryptoStore(
            indexedDB, "matrix-js-sdk:crypto",
        );
    }

    return createClient({
        ...storeOpts,
        ...opts,
    });
}
