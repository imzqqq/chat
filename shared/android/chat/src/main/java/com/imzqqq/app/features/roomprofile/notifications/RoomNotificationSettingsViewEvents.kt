package com.imzqqq.app.features.roomprofile.notifications

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class RoomNotificationSettingsViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : RoomNotificationSettingsViewEvents()
}
