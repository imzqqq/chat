package com.imzqqq.app.features.roomprofile

import com.imzqqq.app.core.platform.VectorSharedAction

/**
 * Supported navigation actions for [RoomProfileActivity]
 */
sealed class RoomProfileSharedAction : VectorSharedAction {
    object OpenRoomSettings : RoomProfileSharedAction()
    object OpenRoomAliasesSettings : RoomProfileSharedAction()
    object OpenRoomPermissionsSettings : RoomProfileSharedAction()
    object OpenRoomUploads : RoomProfileSharedAction()
    object OpenRoomMembers : RoomProfileSharedAction()
    object OpenBannedRoomMembers : RoomProfileSharedAction()
    object OpenRoomNotificationSettings : RoomProfileSharedAction()
}
