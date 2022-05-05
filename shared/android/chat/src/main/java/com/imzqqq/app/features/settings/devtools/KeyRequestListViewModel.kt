@file:Suppress("DEPRECATION")

package com.imzqqq.app.features.settings.devtools

import androidx.lifecycle.asFlow
import androidx.paging.PagedList
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
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
import org.matrix.android.sdk.internal.crypto.IncomingRoomKeyRequest
import org.matrix.android.sdk.internal.crypto.OutgoingRoomKeyRequest

data class KeyRequestListViewState(
        val incomingRequests: Async<PagedList<IncomingRoomKeyRequest>> = Uninitialized,
        val outgoingRoomKeyRequests: Async<PagedList<OutgoingRoomKeyRequest>> = Uninitialized
) : MavericksState

class KeyRequestListViewModel @AssistedInject constructor(@Assisted initialState: KeyRequestListViewState,
                                                          private val session: Session) :
        VectorViewModel<KeyRequestListViewState, EmptyAction, EmptyViewEvents>(initialState) {

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            session.cryptoService().getOutgoingRoomKeyRequestsPaged().asFlow()
                    .execute {
                        copy(outgoingRoomKeyRequests = it)
                    }

            session.cryptoService().getIncomingRoomKeyRequestsPaged()
                    .asFlow()
                    .execute {
                        copy(incomingRequests = it)
                    }
        }
    }

    override fun handle(action: EmptyAction) {}

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<KeyRequestListViewModel, KeyRequestListViewState> {
        override fun create(initialState: KeyRequestListViewState): KeyRequestListViewModel
    }

    companion object : MavericksViewModelFactory<KeyRequestListViewModel, KeyRequestListViewState> by hiltMavericksViewModelFactory()
}
