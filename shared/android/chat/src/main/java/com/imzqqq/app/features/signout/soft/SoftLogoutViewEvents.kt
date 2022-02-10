package com.imzqqq.app.features.signout.soft

import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for SoftLogout
 */
sealed class SoftLogoutViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : SoftLogoutViewEvents()

    data class ErrorNotSameUser(val currentUserId: String, val newUserId: String) : SoftLogoutViewEvents()
    object ClearData : SoftLogoutViewEvents()
}
