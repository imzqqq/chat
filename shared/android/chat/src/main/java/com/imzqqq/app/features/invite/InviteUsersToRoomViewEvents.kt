package com.imzqqq.app.features.invite

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class InviteUsersToRoomViewEvents : VectorViewEvents {
    object Loading : InviteUsersToRoomViewEvents()
    data class Failure(val throwable: Throwable) : InviteUsersToRoomViewEvents()
    data class Success(val successMessage: String) : InviteUsersToRoomViewEvents()
}
