package com.imzqqq.app.features.room

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class RequireActiveMembershipViewEvents : VectorViewEvents {
    data class RoomLeft(val leftMessage: String?) : RequireActiveMembershipViewEvents()
}
