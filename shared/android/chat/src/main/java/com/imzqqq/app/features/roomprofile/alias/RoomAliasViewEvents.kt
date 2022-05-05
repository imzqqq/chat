package com.imzqqq.app.features.roomprofile.alias

import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for room settings screen
 */
sealed class RoomAliasViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : RoomAliasViewEvents()
    object Success : RoomAliasViewEvents()
}
