package com.imzqqq.app.features.roomdirectory

import com.imzqqq.app.core.platform.VectorSharedAction

/**
 * Supported navigation actions for [RoomDirectoryActivity]
 */
sealed class RoomDirectorySharedAction : VectorSharedAction {
    object Back : RoomDirectorySharedAction()
    object CreateRoom : RoomDirectorySharedAction()
    object Close : RoomDirectorySharedAction()
    object ChangeProtocol : RoomDirectorySharedAction()
}
