package com.imzqqq.app.features.login2

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.auth.registration.FlowResult

/**
 * Transient events for Login
 */
sealed class LoginViewEvents2 : VectorViewEvents {
    data class Failure(val throwable: Throwable) : LoginViewEvents2()

    data class RegistrationFlowResult(val flowResult: FlowResult, val isRegistrationStarted: Boolean) : LoginViewEvents2()
    object OutdatedHomeserver : LoginViewEvents2()

    // Navigation event
    object OpenSigninPasswordScreen : LoginViewEvents2()
    object OpenSignupPasswordScreen : LoginViewEvents2()

    object OpenSignInEnterIdentifierScreen : LoginViewEvents2()

    object OpenSignUpChooseUsernameScreen : LoginViewEvents2()
    object OpenSignInWithAnythingScreen : LoginViewEvents2()

    object OpenSsoOnlyScreen : LoginViewEvents2()

    object OpenServerSelection : LoginViewEvents2()
    object OpenHomeServerUrlFormScreen : LoginViewEvents2()

    object OpenResetPasswordScreen : LoginViewEvents2()
    object OnResetPasswordSendThreePidDone : LoginViewEvents2()
    object OnResetPasswordMailConfirmationSuccess : LoginViewEvents2()
    object OnResetPasswordMailConfirmationSuccessDone : LoginViewEvents2()

    object CancelRegistration : LoginViewEvents2()

    data class OnLoginModeNotSupported(val supportedTypes: List<String>) : LoginViewEvents2()

    data class OnSendEmailSuccess(val email: String) : LoginViewEvents2()
    data class OnSendMsisdnSuccess(val msisdn: String) : LoginViewEvents2()

    data class OnWebLoginError(val errorCode: Int, val description: String, val failingUrl: String) : LoginViewEvents2()

    data class OnSessionCreated(val newAccount: Boolean) : LoginViewEvents2()

    object Finish : LoginViewEvents2()
}
