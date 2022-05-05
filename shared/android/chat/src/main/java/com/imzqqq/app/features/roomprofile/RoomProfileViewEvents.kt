package com.imzqqq.app.features.roomprofile

import androidx.core.content.pm.ShortcutInfoCompat
import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for RoomProfile
 */
sealed class RoomProfileViewEvents : VectorViewEvents {
    data class Loading(val message: CharSequence? = null) : RoomProfileViewEvents()
    data class Failure(val throwable: Throwable) : RoomProfileViewEvents()

    data class ShareRoomProfile(val permalink: String) : RoomProfileViewEvents()
    data class OnShortcutReady(val shortcutInfo: ShortcutInfoCompat) : RoomProfileViewEvents()
}
