package com.imzqqq.app.features.settings.devices

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.auth.registration.RegistrationFlowResponse
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.internal.crypto.model.CryptoDeviceInfo
import org.matrix.android.sdk.internal.crypto.model.rest.DeviceInfo

/**
 * Transient events for Ignored users screen
 */
sealed class DevicesViewEvents : VectorViewEvents {
    data class Loading(val message: CharSequence? = null) : DevicesViewEvents()

//    object HideLoading : DevicesViewEvents()
    data class Failure(val throwable: Throwable) : DevicesViewEvents()

//    object RequestPassword : DevicesViewEvents()

    data class RequestReAuth(val registrationFlowResponse: RegistrationFlowResponse, val lastErrorCode: String?) : DevicesViewEvents()

    data class PromptRenameDevice(val deviceInfo: DeviceInfo) : DevicesViewEvents()

    data class ShowVerifyDevice(
            val userId: String,
            val transactionId: String?
    ) : DevicesViewEvents()

    data class SelfVerification(
            val session: Session
    ) : DevicesViewEvents()

    data class ShowManuallyVerify(val cryptoDeviceInfo: CryptoDeviceInfo) : DevicesViewEvents()
}
