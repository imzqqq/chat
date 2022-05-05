package com.imzqqq.app.features.login2

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.core.extensions.toReducedUrl
import com.imzqqq.app.features.login.LoginMode
import org.matrix.android.sdk.api.MatrixPatterns
import org.matrix.android.sdk.api.auth.login.LoginProfileInfo

data class LoginViewState2(
        val isLoading: Boolean = false,

        // User choices
        @PersistState
        val signMode: SignMode2 = SignMode2.Unknown,
        @PersistState
        val userName: String? = null,
        @PersistState
        val resetPasswordEmail: String? = null,
        @PersistState
        val homeServerUrlFromUser: String? = null,

        // Can be modified after a Wellknown request
        @PersistState
        val homeServerUrl: String? = null,

        // For SSO session recovery
        @PersistState
        val deviceId: String? = null,

        // Network result
        val loginProfileInfo: Async<LoginProfileInfo> = Uninitialized,

        // Network result
        @PersistState
        val loginMode: LoginMode = LoginMode.Unknown,

        // From database
        val knownCustomHomeServersUrls: List<String> = emptyList()
) : MavericksState {

    // Pending user identifier
    fun userIdentifier(): String {
        return if (userName != null && MatrixPatterns.isUserId(userName)) {
            userName
        } else {
            "@$userName:${homeServerUrlFromUser.toReducedUrl()}"
        }
    }
}
