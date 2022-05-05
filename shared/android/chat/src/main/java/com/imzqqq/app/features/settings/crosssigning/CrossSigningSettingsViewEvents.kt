package com.imzqqq.app.features.settings.crosssigning

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.auth.registration.RegistrationFlowResponse

/**
 * Transient events for cross signing settings screen
 */
sealed class CrossSigningSettingsViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : CrossSigningSettingsViewEvents()
    data class RequestReAuth(val registrationFlowResponse: RegistrationFlowResponse, val lastErrorCode: String?) : CrossSigningSettingsViewEvents()
    data class ShowModalWaitingView(val status: String?) : CrossSigningSettingsViewEvents()
    object HideModalWaitingView : CrossSigningSettingsViewEvents()
}
