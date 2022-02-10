package com.imzqqq.app.features.call

import com.imzqqq.app.core.platform.VectorViewEvents
import com.imzqqq.app.features.call.audio.CallAudioManager
import org.matrix.android.sdk.api.session.call.TurnServerResponse

sealed class VectorCallViewEvents : VectorViewEvents {

    data class ConnectionTimeout(val turn: TurnServerResponse?) : VectorCallViewEvents()
    data class ShowSoundDeviceChooser(
            val available: Set<CallAudioManager.Device>,
            val current: CallAudioManager.Device
    ) : VectorCallViewEvents()
    object ShowDialPad : VectorCallViewEvents()
    object ShowCallTransferScreen : VectorCallViewEvents()
//    data class CallAnswered(val content: CallAnswerContent) : VectorCallViewEvents()
//    data class CallHangup(val content: CallHangupContent) : VectorCallViewEvents()
//    object CallAccepted : VectorCallViewEvents()
}
