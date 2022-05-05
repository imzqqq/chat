package com.imzqqq.app.features.crypto.recover

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.auth.registration.RegistrationFlowResponse

sealed class BootstrapViewEvents : VectorViewEvents {
    data class Dismiss(val success: Boolean) : BootstrapViewEvents()
    data class ModalError(val error: String) : BootstrapViewEvents()
    object RecoveryKeySaved : BootstrapViewEvents()
    data class SkipBootstrap(val genKeyOption: Boolean = true) : BootstrapViewEvents()
    data class RequestReAuth(val flowResponse: RegistrationFlowResponse, val lastErrorCode: String?) : BootstrapViewEvents()
}
