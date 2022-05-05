package com.imzqqq.app.features.settings.threepids

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.auth.registration.RegistrationFlowResponse

sealed class ThreePidsSettingsViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : ThreePidsSettingsViewEvents()

//    object RequestPassword : ThreePidsSettingsViewEvents()
    data class RequestReAuth(val registrationFlowResponse: RegistrationFlowResponse, val lastErrorCode: String?) : ThreePidsSettingsViewEvents()
}
