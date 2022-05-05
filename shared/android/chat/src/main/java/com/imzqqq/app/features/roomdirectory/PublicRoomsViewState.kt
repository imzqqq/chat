package com.imzqqq.app.features.roomdirectory

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.room.members.ChangeMembershipState
import org.matrix.android.sdk.api.session.room.model.roomdirectory.PublicRoom

data class PublicRoomsViewState(
        // The current filter
        val currentFilter: String = "",
        // Store cumul of pagination result
        val publicRooms: List<PublicRoom> = emptyList(),
        // Current pagination request
        val asyncPublicRoomsRequest: Async<Unit> = Uninitialized,
        // True if more result are available server side
        val hasMore: Boolean = false,
        // Set of joined roomId,
        val joinedRoomsIds: Set<String> = emptySet(),
        // keys are room alias or roomId
        val changeMembershipStates: Map<String, ChangeMembershipState> = emptyMap(),
        val roomDirectoryData: RoomDirectoryData = RoomDirectoryData()
) : MavericksState
