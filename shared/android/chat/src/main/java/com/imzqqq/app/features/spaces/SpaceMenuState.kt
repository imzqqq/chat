package com.imzqqq.app.features.spaces

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class SpaceMenuState(
        val spaceId: String,
        val spaceSummary: RoomSummary? = null,
        val canEditSettings: Boolean = false,
        val canInvite: Boolean = false,
        val canAddChild: Boolean = false,
        val isLastAdmin: Boolean = false,
        val leaveMode: LeaveMode = LeaveMode.LEAVE_NONE,
        val leavingState: Async<Unit> = Uninitialized
) : MavericksState {
    constructor(args: SpaceBottomSheetSettingsArgs) : this(spaceId = args.spaceId)

    enum class LeaveMode {
        LEAVE_ALL, LEAVE_NONE, LEAVE_SELECTED
    }
}
