package com.imzqqq.app.features.userdirectory

import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for invite users to room screen
 */
sealed class UserListViewEvents : VectorViewEvents {
    data class OpenShareMatrixToLink(val link: String) : UserListViewEvents()
}
