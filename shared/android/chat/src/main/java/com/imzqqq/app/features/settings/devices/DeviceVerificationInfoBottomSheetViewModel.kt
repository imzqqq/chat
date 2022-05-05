package com.imzqqq.app.features.settings.devices

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.EmptyAction
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import kotlinx.coroutines.flow.map
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.flow.flow
import org.matrix.android.sdk.internal.crypto.model.rest.DeviceInfo

class DeviceVerificationInfoBottomSheetViewModel @AssistedInject constructor(@Assisted initialState: DeviceVerificationInfoBottomSheetViewState,
                                                                             val session: Session
) : VectorViewModel<DeviceVerificationInfoBottomSheetViewState, EmptyAction, EmptyViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<DeviceVerificationInfoBottomSheetViewModel, DeviceVerificationInfoBottomSheetViewState> {
        override fun create(initialState: DeviceVerificationInfoBottomSheetViewState): DeviceVerificationInfoBottomSheetViewModel
    }

    companion object : MavericksViewModelFactory<DeviceVerificationInfoBottomSheetViewModel, DeviceVerificationInfoBottomSheetViewState>
    by hiltMavericksViewModelFactory()

    init {

        setState {
            copy(
                    hasAccountCrossSigning = session.cryptoService().crossSigningService().isCrossSigningInitialized(),
                    accountCrossSigningIsTrusted = session.cryptoService().crossSigningService().isCrossSigningVerified(),
                    isRecoverySetup = session.sharedSecretStorageService.isRecoverySetup()
            )
        }
        session.flow().liveCrossSigningInfo(session.myUserId)
                .execute {
                    copy(
                            hasAccountCrossSigning = it.invoke()?.getOrNull() != null,
                            accountCrossSigningIsTrusted = it.invoke()?.getOrNull()?.isTrusted() == true
                    )
                }

        session.flow().liveUserCryptoDevices(session.myUserId)
                .map { list ->
                    list.firstOrNull { it.deviceId == initialState.deviceId }
                }
                .execute {
                    copy(
                            cryptoDeviceInfo = it,
                            isMine = it.invoke()?.deviceId == session.sessionParams.deviceId
                    )
                }

        session.flow().liveUserCryptoDevices(session.myUserId)
                .map { it.size }
                .execute {
                    copy(
                            hasOtherSessions = it.invoke() ?: 0 > 1
                    )
                }

        setState {
            copy(deviceInfo = Loading())
        }

        session.flow().liveMyDevicesInfo()
                .map { devices ->
                    devices.firstOrNull { it.deviceId == initialState.deviceId } ?: DeviceInfo(deviceId = initialState.deviceId)
                }
                .execute {
                    copy(deviceInfo = it)
                }
    }

    override fun handle(action: EmptyAction) {
    }
}
