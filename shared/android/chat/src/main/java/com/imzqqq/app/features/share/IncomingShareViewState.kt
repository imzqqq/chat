package com.imzqqq.app.features.share

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class IncomingShareViewState(
        val sharedData: SharedData? = null,
        val roomSummaries: Async<List<RoomSummary>> = Uninitialized,
        val filteredRoomSummaries: Async<List<RoomSummary>> = Uninitialized,
        val selectedRoomIds: Set<String> = emptySet(),
        val isInMultiSelectionMode: Boolean = false
) : MavericksState
