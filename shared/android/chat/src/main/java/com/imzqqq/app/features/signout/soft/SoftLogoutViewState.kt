package com.imzqqq.app.features.signout.soft

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.features.login.LoginMode

data class SoftLogoutViewState(
        val asyncHomeServerLoginFlowRequest: Async<LoginMode> = Uninitialized,
        val asyncLoginAction: Async<Unit> = Uninitialized,
        val homeServerUrl: String,
        val userId: String,
        val deviceId: String,
        val userDisplayName: String,
        val hasUnsavedKeys: Boolean,
        val enteredPassword: String = ""
) : MavericksState {

    fun isLoading(): Boolean {
        return asyncLoginAction is Loading ||
                // Keep loading when it is success because of the delay to switch to the next Activity
                asyncLoginAction is Success
    }
}
