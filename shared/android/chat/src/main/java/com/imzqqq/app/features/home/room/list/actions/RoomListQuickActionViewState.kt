package com.imzqqq.app.features.home.room.list.actions

import com.imzqqq.app.features.roomprofile.notifications.RoomNotificationSettingsViewState

data class RoomListQuickActionViewState(
        val roomListActionsArgs: RoomListActionsArgs,
        val notificationSettingsViewState: RoomNotificationSettingsViewState
)
