package com.imzqqq.app.features.roommemberprofile.devices

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.internal.crypto.model.CryptoDeviceInfo

sealed class DeviceListAction : VectorViewModelAction {
    data class SelectDevice(val device: CryptoDeviceInfo) : DeviceListAction()
    object DeselectDevice : DeviceListAction()

    data class ManuallyVerify(val deviceId: String) : DeviceListAction()
}
