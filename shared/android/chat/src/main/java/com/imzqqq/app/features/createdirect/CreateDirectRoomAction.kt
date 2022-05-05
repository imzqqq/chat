package com.imzqqq.app.features.createdirect

import com.imzqqq.app.core.platform.VectorViewModelAction
import com.imzqqq.app.features.userdirectory.PendingSelection

sealed class CreateDirectRoomAction : VectorViewModelAction {
    data class CreateRoomAndInviteSelectedUsers(
            val selections: Set<PendingSelection>
    ) : CreateDirectRoomAction()
}
