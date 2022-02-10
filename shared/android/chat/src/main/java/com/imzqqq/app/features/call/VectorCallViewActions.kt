package com.imzqqq.app.features.call

import com.imzqqq.app.core.platform.VectorViewModelAction
import com.imzqqq.app.features.call.audio.CallAudioManager

sealed class VectorCallViewActions : VectorViewModelAction {
    object EndCall : VectorCallViewActions()
    object AcceptCall : VectorCallViewActions()
    object DeclineCall : VectorCallViewActions()
    object ToggleMute : VectorCallViewActions()
    object ToggleVideo : VectorCallViewActions()
    object ToggleHoldResume : VectorCallViewActions()
    data class ChangeAudioDevice(val device: CallAudioManager.Device) : VectorCallViewActions()
    object OpenDialPad : VectorCallViewActions()
    data class SendDtmfDigit(val digit: String) : VectorCallViewActions()
    data class SwitchCall(val callArgs: CallArgs) : VectorCallViewActions()

    object SwitchSoundDevice : VectorCallViewActions()
    object HeadSetButtonPressed : VectorCallViewActions()
    object ToggleCamera : VectorCallViewActions()
    object ToggleHDSD : VectorCallViewActions()
    object InitiateCallTransfer : VectorCallViewActions()
    object TransferCall : VectorCallViewActions()
}
