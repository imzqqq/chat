package com.imzqqq.app.features.sync.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.imzqqq.app.R
import com.imzqqq.app.core.utils.isAirplaneModeOn
import com.imzqqq.app.databinding.ViewSyncStateBinding
import org.matrix.android.sdk.api.session.initsync.SyncStatusService
import org.matrix.android.sdk.api.session.sync.SyncState

class SyncStateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    LinearLayout(context, attrs, defStyle) {

    private val views: ViewSyncStateBinding

    init {
        inflate(context, R.layout.view_sync_state, this)
        views = ViewSyncStateBinding.bind(this)
        orientation = VERTICAL
    }

    @SuppressLint("SetTextI18n")
    fun render(newState: SyncState,
               incrementalSyncStatus: SyncStatusService.Status.IncrementalSyncStatus,
               pushCounter: Int,
               showDebugInfo: Boolean
    ) {
        views.syncStateDebugInfo.isVisible = showDebugInfo
        if (showDebugInfo) {
            views.syncStateDebugInfoText.text =
                    "Sync thread : ${newState.toHumanReadable()}\nSync request: ${incrementalSyncStatus.toHumanReadable()}"
            views.syncStateDebugInfoPushCounter.text =
                    "Push: $pushCounter"
        }
        views.syncStateProgressBar.isVisible = newState is SyncState.Running && newState.afterPause

        if (newState == SyncState.NoNetwork) {
            val isAirplaneModeOn = isAirplaneModeOn(context)
            views.syncStateNoNetwork.isVisible = isAirplaneModeOn.not()
            views.syncStateNoNetworkAirplane.isVisible = isAirplaneModeOn
        } else {
            views.syncStateNoNetwork.isVisible = false
            views.syncStateNoNetworkAirplane.isVisible = false
        }
    }

    private fun SyncState.toHumanReadable(): String {
        return when (this) {
            SyncState.Idle         -> "Idle"
            SyncState.InvalidToken -> "InvalidToken"
            SyncState.Killed       -> "Killed"
            SyncState.Killing      -> "Killing"
            SyncState.NoNetwork    -> "NoNetwork"
            SyncState.Paused       -> "Paused"
            is SyncState.Running   -> "$this"
        }
    }

    private fun SyncStatusService.Status.IncrementalSyncStatus.toHumanReadable(): String {
        return when (this) {
            SyncStatusService.Status.IncrementalSyncIdle       -> "Idle"
            is SyncStatusService.Status.IncrementalSyncParsing -> "Parsing ${this.rooms} room(s) ${this.toDevice} toDevice(s)"
            SyncStatusService.Status.IncrementalSyncError      -> "Error"
            SyncStatusService.Status.IncrementalSyncDone       -> "Done"
            else                                               -> "?"
        }
    }
}
