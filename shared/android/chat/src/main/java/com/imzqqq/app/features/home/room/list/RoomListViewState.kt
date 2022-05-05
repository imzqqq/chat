package com.imzqqq.app.features.home.room.list

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.RoomGroupingMethod
import com.imzqqq.app.features.home.RoomListDisplayMode
import org.matrix.android.sdk.api.session.room.members.ChangeMembershipState
import org.matrix.android.sdk.api.session.room.model.SpaceChildInfo

data class RoomListViewState(
        val displayMode: RoomListDisplayMode,
        val roomFilter: String = "",
        val roomMembershipChanges: Map<String, ChangeMembershipState> = emptyMap(),
        val asyncSuggestedRooms: Async<List<SpaceChildInfo>> = Uninitialized,
        val currentUserName: String? = null,
        val currentRoomGrouping: Async<RoomGroupingMethod> = Uninitialized
) : MavericksState {

    constructor(args: RoomListParams) : this(displayMode = args.displayMode)
}
