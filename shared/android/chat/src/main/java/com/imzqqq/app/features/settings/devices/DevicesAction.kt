package com.imzqqq.app.features.settings.devices

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.internal.crypto.model.CryptoDeviceInfo

sealed class DevicesAction : VectorViewModelAction {
    object Refresh : DevicesAction()
    data class Delete(val deviceId: String) : DevicesAction()

//    data class Password(val password: String) : DevicesAction()
    data class Rename(val deviceId: String, val newName: String) : DevicesAction()

    data class PromptRename(val deviceId: String) : DevicesAction()
    data class VerifyMyDevice(val deviceId: String) : DevicesAction()
    data class VerifyMyDeviceManually(val deviceId: String) : DevicesAction()
    object CompleteSecurity : DevicesAction()
    data class MarkAsManuallyVerified(val cryptoDeviceInfo: CryptoDeviceInfo) : DevicesAction()

    object SsoAuthDone : DevicesAction()
    data class PasswordAuthDone(val password: String) : DevicesAction()
    object ReAuthCancelled : DevicesAction()
}
