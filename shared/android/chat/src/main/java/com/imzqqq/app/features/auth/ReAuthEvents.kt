package com.imzqqq.app.features.auth

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class ReAuthEvents : VectorViewEvents {
    data class OpenSsoURl(val url: String) : ReAuthEvents()
    object Dismiss : ReAuthEvents()
    data class PasswordFinishSuccess(val passwordSafeForIntent: String) : ReAuthEvents()
}
