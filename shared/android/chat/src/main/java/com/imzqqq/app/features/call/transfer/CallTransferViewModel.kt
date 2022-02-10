package com.imzqqq.app.features.call.transfer

import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.call.dialpad.DialPadLookup
import com.imzqqq.app.features.call.webrtc.WebRtcCall
import com.imzqqq.app.features.call.webrtc.WebRtcCallManager
import com.imzqqq.app.features.createdirect.DirectRoomHelper
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.extensions.orFalse
import org.matrix.android.sdk.api.session.call.CallState
import org.matrix.android.sdk.api.session.call.MxCall

class CallTransferViewModel @AssistedInject constructor(@Assisted initialState: CallTransferViewState,
                                                        private val dialPadLookup: DialPadLookup,
                                                        private val directRoomHelper: DirectRoomHelper,
                                                        private val callManager: WebRtcCallManager) :
    VectorViewModel<CallTransferViewState, CallTransferAction, CallTransferViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<CallTransferViewModel, CallTransferViewState> {
        override fun create(initialState: CallTransferViewState): CallTransferViewModel
    }

    companion object : MavericksViewModelFactory<CallTransferViewModel, CallTransferViewState> by hiltMavericksViewModelFactory()

    private val call = callManager.getCallById(initialState.callId)
    private val callListener = object : WebRtcCall.Listener {
        override fun onStateUpdate(call: MxCall) {
            if (call.state is CallState.Ended) {
                _viewEvents.post(CallTransferViewEvents.Dismiss)
            }
        }
    }

    init {
        if (call == null) {
            _viewEvents.post(CallTransferViewEvents.Dismiss)
        } else {
            call.addListener(callListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        call?.removeListener(callListener)
    }

    override fun handle(action: CallTransferAction) {
        when (action) {
            is CallTransferAction.ConnectWithUserId      -> connectWithUserId(action)
            is CallTransferAction.ConnectWithPhoneNumber -> connectWithPhoneNumber(action)
        }.exhaustive
    }

    private fun connectWithUserId(action: CallTransferAction.ConnectWithUserId) {
        viewModelScope.launch {
            try {
                if (action.consultFirst) {
                    val dmRoomId = directRoomHelper.ensureDMExists(action.selectedUserId)
                    callManager.startOutgoingCall(
                            nativeRoomId = dmRoomId,
                            otherUserId = action.selectedUserId,
                            isVideoCall = call?.mxCall?.isVideoCall.orFalse(),
                            transferee = call
                    )
                } else {
                    call?.transferToUser(action.selectedUserId, null)
                }
                _viewEvents.post(CallTransferViewEvents.Dismiss)
            } catch (failure: Throwable) {
                _viewEvents.post(CallTransferViewEvents.FailToTransfer)
            }
        }
    }

    private fun connectWithPhoneNumber(action: CallTransferAction.ConnectWithPhoneNumber) {
        viewModelScope.launch {
            try {
                _viewEvents.post(CallTransferViewEvents.Loading)
                val result = dialPadLookup.lookupPhoneNumber(action.phoneNumber)
                if (action.consultFirst) {
                    callManager.startOutgoingCall(
                            nativeRoomId = result.roomId,
                            otherUserId = result.userId,
                            isVideoCall = call?.mxCall?.isVideoCall.orFalse(),
                            transferee = call
                    )
                } else {
                    call?.transferToUser(result.userId, result.roomId)
                }
                _viewEvents.post(CallTransferViewEvents.Dismiss)
            } catch (failure: Throwable) {
                _viewEvents.post(CallTransferViewEvents.FailToTransfer)
            }
        }
    }
}
