package com.imzqqq.app.features.roomdirectory

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class RoomDirectoryAction : VectorViewModelAction {
    data class SetRoomDirectoryData(val roomDirectoryData: RoomDirectoryData) : RoomDirectoryAction()
    data class FilterWith(val filter: String) : RoomDirectoryAction()
    object LoadMore : RoomDirectoryAction()
    data class JoinRoom(val roomId: String) : RoomDirectoryAction()
}
