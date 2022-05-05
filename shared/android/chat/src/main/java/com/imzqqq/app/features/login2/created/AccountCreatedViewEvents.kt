package com.imzqqq.app.features.login2.created

import com.imzqqq.app.core.platform.VectorViewEvents

/**
 * Transient events for Account Created
 */
sealed class AccountCreatedViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : AccountCreatedViewEvents()
}
