package com.imzqqq.app.features.roomprofile

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.room.notification.RoomNotificationState

sealed class RoomProfileAction : VectorViewModelAction {
    object EnableEncryption : RoomProfileAction()
    object LeaveRoom : RoomProfileAction()
    data class ChangeRoomNotificationState(val notificationState: RoomNotificationState) : RoomProfileAction()
    object ShareRoomProfile : RoomProfileAction()
    object CreateShortcut : RoomProfileAction()
}
