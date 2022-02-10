package com.imzqqq.app.features.room

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class RequireActiveMembershipAction : VectorViewModelAction {
    data class ChangeRoom(val roomId: String) : RequireActiveMembershipAction()
}
