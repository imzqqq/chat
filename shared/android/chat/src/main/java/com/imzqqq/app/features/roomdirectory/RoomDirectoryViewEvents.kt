package com.imzqqq.app.features.roomdirectory

import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for room directory screen
 */
sealed class RoomDirectoryViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : RoomDirectoryViewEvents()
}
