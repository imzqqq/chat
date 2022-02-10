package com.imzqqq.app.features.home.room.detail.timeline.edithistory

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.EmptyAction
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.crypto.MXCryptoError
import org.matrix.android.sdk.api.session.events.model.isReply
import org.matrix.android.sdk.internal.crypto.algorithms.olm.OlmDecryptionResult
import timber.log.Timber
import java.util.UUID

class ViewEditHistoryViewModel @AssistedInject constructor(
        @Assisted initialState: ViewEditHistoryViewState,
        private val session: Session
) : VectorViewModel<ViewEditHistoryViewState, EmptyAction, EmptyViewEvents>(initialState) {

    private val roomId = initialState.roomId
    private val eventId = initialState.eventId
    private val room = session.getRoom(roomId)
            ?: throw IllegalStateException("Shouldn't use this ViewModel without a room")

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<ViewEditHistoryViewModel, ViewEditHistoryViewState> {
        override fun create(initialState: ViewEditHistoryViewState): ViewEditHistoryViewModel
    }

    companion object : MavericksViewModelFactory<ViewEditHistoryViewModel, ViewEditHistoryViewState> by hiltMavericksViewModelFactory()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        setState { copy(editList = Loading()) }

        viewModelScope.launch {
            val data = try {
                room.fetchEditHistory(eventId)
            } catch (failure: Throwable) {
                setState {
                    copy(editList = Fail(failure))
                }
                return@launch
            }

            var originalIsReply = false

            data.forEach { event ->
                val timelineID = event.roomId + UUID.randomUUID().toString()
                // We need to check encryption
                if (event.isEncrypted() && event.mxDecryptionResult == null) {
                    // for now decrypt sync
                    try {
                        val result = session.cryptoService().decryptEvent(event, timelineID)
                        event.mxDecryptionResult = OlmDecryptionResult(
                                payload = result.clearEvent,
                                senderKey = result.senderCurve25519Key,
                                keysClaimed = result.claimedEd25519Key?.let { k -> mapOf("ed25519" to k) },
                                forwardingCurve25519KeyChain = result.forwardingCurve25519KeyChain
                        )
                    } catch (e: MXCryptoError) {
                        Timber.w("Failed to decrypt event in history")
                    }
                }

                if (event.eventId == eventId) {
                    originalIsReply = event.isReply()
                }
            }
            setState {
                copy(
                        editList = Success(data),
                        isOriginalAReply = originalIsReply
                )
            }
        }
    }

    override fun handle(action: EmptyAction) {
        // No op
    }
}
