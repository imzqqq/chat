package com.imzqqq.app.features.settings.devtools

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.core.platform.VectorViewModelAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session

sealed class KeyRequestAction : VectorViewModelAction {
    data class ExportAudit(val uri: Uri) : KeyRequestAction()
}

sealed class KeyRequestEvents : VectorViewEvents {
    data class SaveAudit(val uri: Uri, val raw: String) : KeyRequestEvents()
}

data class KeyRequestViewState(
        val exporting: Async<Unit> = Uninitialized
) : MavericksState

class KeyRequestViewModel @AssistedInject constructor(
        @Assisted initialState: KeyRequestViewState,
        private val session: Session) :
    VectorViewModel<KeyRequestViewState, KeyRequestAction, KeyRequestEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<KeyRequestViewModel, KeyRequestViewState> {
        override fun create(initialState: KeyRequestViewState): KeyRequestViewModel
    }

    companion object : MavericksViewModelFactory<KeyRequestViewModel, KeyRequestViewState> by hiltMavericksViewModelFactory()

    override fun handle(action: KeyRequestAction) {
        when (action) {
            is KeyRequestAction.ExportAudit -> exportAudit(action)
        }.exhaustive
    }

    private fun exportAudit(action: KeyRequestAction.ExportAudit) {
        setState {
            copy(exporting = Loading())
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // this can take long
                val eventList = session.cryptoService().getGossipingEvents()
                // clean it a bit to
                val raw = GossipingEventsSerializer().serialize(eventList)
                setState {
                    copy(exporting = Success(Unit))
                }
                _viewEvents.post(KeyRequestEvents.SaveAudit(action.uri, raw))
            } catch (error: Throwable) {
                setState {
                    copy(exporting = Fail(error))
                }
            }
        }
    }
}
