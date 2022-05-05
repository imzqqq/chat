package com.imzqqq.app.features.home.room.detail

import com.imzqqq.app.core.platform.VectorSharedAction

/**
 * Supported navigation actions for [RoomDetailActivity]
 */
sealed class RoomDetailSharedAction : VectorSharedAction {
    data class SwitchToRoom(val roomId: String) : RoomDetailSharedAction()
}
