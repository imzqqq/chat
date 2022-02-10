package com.imzqqq.app.features.settings.devices

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.internal.crypto.model.CryptoDeviceInfo
import org.matrix.android.sdk.internal.crypto.model.rest.DeviceInfo

data class DeviceVerificationInfoBottomSheetViewState(
        val deviceId: String,
        val cryptoDeviceInfo: Async<CryptoDeviceInfo?> = Uninitialized,
        val deviceInfo: Async<DeviceInfo> = Uninitialized,
        val hasAccountCrossSigning: Boolean = false,
        val accountCrossSigningIsTrusted: Boolean = false,
        val isMine: Boolean = false,
        val hasOtherSessions: Boolean = false,
        val isRecoverySetup: Boolean = false
) : MavericksState {

    constructor(args: DeviceVerificationInfoArgs) : this(deviceId = args.deviceId)

    val canVerifySession = hasOtherSessions || isRecoverySetup
}
