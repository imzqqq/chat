package com.imzqqq.app.features.roomprofile.banned

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.features.roomprofile.RoomProfileArgs
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary
import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class RoomBannedMemberListViewState(
        val roomId: String,
        val roomSummary: Async<RoomSummary> = Uninitialized,
        val bannedMemberSummaries: Async<List<RoomMemberSummary>> = Uninitialized,
        val filter: String = "",
        val onGoingModerationAction: List<String> = emptyList(),
        val canUserBan: Boolean = false
) : MavericksState {

    constructor(args: RoomProfileArgs) : this(roomId = args.roomId)
}
