// TODO: Merge this with sync.js once converted

export enum SyncState {
    Error = "ERROR",
    Prepared = "PREPARED",
    Stopped = "STOPPED",
    Syncing = "SYNCING",
    Catchup = "CATCHUP",
    Reconnecting = "RECONNECTING",
}
