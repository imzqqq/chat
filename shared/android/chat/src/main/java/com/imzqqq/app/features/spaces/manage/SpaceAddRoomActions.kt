package com.imzqqq.app.features.spaces.manage

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.room.model.RoomSummary

sealed class SpaceAddRoomActions : VectorViewModelAction {
    data class UpdateFilter(val filter: String) : SpaceAddRoomActions()
    data class ToggleSelection(val roomSummary: RoomSummary) : SpaceAddRoomActions()
    object Save : SpaceAddRoomActions()
//    object HandleBack : SpaceAddRoomActions()
}
