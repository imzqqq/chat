package com.imzqqq.app.features.spaces.leave

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.features.spaces.SpaceBottomSheetSettingsArgs
import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class SpaceLeaveAdvanceViewState(
        val spaceId: String,
        val spaceSummary: RoomSummary? = null,
        val allChildren: Async<List<RoomSummary>> = Uninitialized,
        val selectedRooms: List<String> = emptyList(),
        val currentFilter: String = "",
        val leaveState: Async<Unit> = Uninitialized
) : MavericksState {
    constructor(args: SpaceBottomSheetSettingsArgs) : this(
            spaceId = args.spaceId
    )
}
