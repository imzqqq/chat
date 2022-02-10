package com.imzqqq.app.features.settings.account.deactivation

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.auth.registration.RegistrationFlowResponse

/**
 * Transient events for deactivate account settings screen
 */
sealed class DeactivateAccountViewEvents : VectorViewEvents {
    data class Loading(val message: CharSequence? = null) : DeactivateAccountViewEvents()
    object InvalidAuth : DeactivateAccountViewEvents()
    data class OtherFailure(val throwable: Throwable) : DeactivateAccountViewEvents()
    object Done : DeactivateAccountViewEvents()
    data class RequestReAuth(val registrationFlowResponse: RegistrationFlowResponse, val lastErrorCode: String?) : DeactivateAccountViewEvents()
}
