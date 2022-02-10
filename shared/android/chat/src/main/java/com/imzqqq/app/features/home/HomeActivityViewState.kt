package com.imzqqq.app.features.home

import com.airbnb.mvrx.MavericksState
import org.matrix.android.sdk.api.session.initsync.SyncStatusService

data class HomeActivityViewState(
        val syncStatusServiceStatus: SyncStatusService.Status = SyncStatusService.Status.Idle
) : MavericksState
