/**
 * Check if an IndexedDB database exists. The only way to do so is to try opening it, so
 * we do that and then delete it did not exist before.
 *
 * @param {Object} indexedDB The `indexedDB` interface
 * @param {string} dbName The database name to test for
 * @returns {boolean} Whether the database exists
 */
export function exists(indexedDB: IDBFactory, dbName: string): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
        let exists = true;
        const req = indexedDB.open(dbName);
        req.onupgradeneeded = () => {
            // Since we did not provide an explicit version when opening, this event
            // should only fire if the DB did not exist before at any version.
            exists = false;
        };
        req.onblocked = () => reject(req.error);
        req.onsuccess = () => {
            const db = req.result;
            db.close();
            if (!exists) {
                // The DB did not exist before, but has been created as part of this
                // existence check. Delete it now to restore previous state. Delete can
                // actually take a while to complete in some browsers, so don't wait for
                // it. This won't block future open calls that a store might issue next to
                // properly set up the DB.
                indexedDB.deleteDatabase(dbName);
            }
            resolve(exists);
        };
        req.onerror = ev => reject(req.error);
    });
}
