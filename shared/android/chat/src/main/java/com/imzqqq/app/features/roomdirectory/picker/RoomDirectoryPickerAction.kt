package com.imzqqq.app.features.roomdirectory.picker

import com.imzqqq.app.core.platform.VectorViewModelAction
import com.imzqqq.app.features.roomdirectory.RoomDirectoryServer

sealed class RoomDirectoryPickerAction : VectorViewModelAction {
    object Retry : RoomDirectoryPickerAction()
    object EnterEditMode : RoomDirectoryPickerAction()
    object ExitEditMode : RoomDirectoryPickerAction()
    data class SetServerUrl(val url: String) : RoomDirectoryPickerAction()
    data class RemoveServer(val roomDirectoryServer: RoomDirectoryServer) : RoomDirectoryPickerAction()

    object Submit : RoomDirectoryPickerAction()
}
