package org.matrix.android.sdk.api.session.initsync

import androidx.lifecycle.LiveData

interface SyncStatusService {

    fun getSyncStatusLive(): LiveData<Status>

    sealed class Status {
        /**
         * For initial sync
         */
        abstract class InitialSyncStatus : Status()

        object Idle : InitialSyncStatus()
        data class Progressing(
                val initSyncStep: InitSyncStep,
                val percentProgress: Int = 0
        ) : InitialSyncStatus()

        /**
         * For incremental sync
         */
        abstract class IncrementalSyncStatus : Status()

        object IncrementalSyncIdle : IncrementalSyncStatus()
        data class IncrementalSyncParsing(
                val rooms: Int,
                val toDevice: Int
        ) : IncrementalSyncStatus()
        object IncrementalSyncError : IncrementalSyncStatus()
        object IncrementalSyncDone : IncrementalSyncStatus()
    }
}
