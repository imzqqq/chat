package com.imzqqq.app.features.spaces.explore

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import org.matrix.android.sdk.api.session.room.members.ChangeMembershipState
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.model.SpaceChildInfo
import org.matrix.android.sdk.api.session.space.SpaceHierarchyData

data class SpaceDirectoryState(
        // The current filter
        val spaceId: String,
        val currentFilter: String = "",
        val apiResults: Map<String, Async<SpaceHierarchyData>> = emptyMap(),
        val currentRootSummary: RoomSummary? = null,
        val childList: List<SpaceChildInfo> = emptyList(),
        val hierarchyStack: List<String> = emptyList(),
        // Set of joined roomId / spaces,
        val joinedRoomsIds: Set<String> = emptySet(),
        // keys are room alias or roomId
        val changeMembershipStates: Map<String, ChangeMembershipState> = emptyMap(),
        val canAddRooms: Boolean = false,
        // cached room summaries of known rooms, we use it because computed room name would be better using it
        val knownRoomSummaries: List<RoomSummary> = emptyList(),
        val paginationStatus: Map<String, Async<Unit>> = emptyMap()
) : MavericksState {
    constructor(args: SpaceDirectoryArgs) : this(
            spaceId = args.spaceId
    )
}
