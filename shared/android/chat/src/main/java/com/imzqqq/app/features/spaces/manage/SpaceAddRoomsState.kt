package com.imzqqq.app.features.spaces.manage

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized

data class SpaceAddRoomsState(
        // The current filter
        val spaceId: String = "",
        val currentFilter: String = "",
        val spaceName: String = "",
        val ignoreRooms: List<String> = emptyList(),
        val isSaving: Async<List<String>> = Uninitialized,
        val shouldShowDMs: Boolean = false,
        val onlyShowSpaces: Boolean = false
//        val selectionList: Map<String, Boolean> = emptyMap()
) : MavericksState {
    constructor(args: SpaceManageArgs) : this(
            spaceId = args.spaceId,
            onlyShowSpaces = args.manageType == ManageType.AddRoomsOnlySpaces
    )
}
