package com.imzqqq.app.features.invite

import com.imzqqq.app.core.platform.VectorViewModelAction
import com.imzqqq.app.features.userdirectory.PendingSelection

sealed class InviteUsersToRoomAction : VectorViewModelAction {
    data class InviteSelectedUsers(val selections: Set<PendingSelection>) : InviteUsersToRoomAction()
}
