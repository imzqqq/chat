package com.imzqqq.app.features.roomdirectory.createroom

import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for room creation screen
 */
sealed class CreateRoomViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : CreateRoomViewEvents()
    object Quit : CreateRoomViewEvents()
}
