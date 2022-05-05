@file:Suppress("DEPRECATION")

package com.imzqqq.app.features.settings.devtools

import androidx.lifecycle.asFlow
import androidx.paging.PagedList
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
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
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.events.model.Event

data class GossipingEventsPaperTrailState(
        val events: Async<PagedList<Event>> = Uninitialized
) : MavericksState

class GossipingEventsPaperTrailViewModel @AssistedInject constructor(@Assisted initialState: GossipingEventsPaperTrailState,
                                                                     private val session: Session) :
    VectorViewModel<GossipingEventsPaperTrailState, EmptyAction, EmptyViewEvents>(initialState) {

    init {
        refresh()
    }

    fun refresh() {
        setState {
            copy(events = Loading())
        }
        session.cryptoService().getGossipingEventsTrail()
                .asFlow()
                .execute {
                    copy(events = it)
                }
    }

    override fun handle(action: EmptyAction) {}

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<GossipingEventsPaperTrailViewModel, GossipingEventsPaperTrailState> {
        override fun create(initialState: GossipingEventsPaperTrailState): GossipingEventsPaperTrailViewModel
    }

    companion object : MavericksViewModelFactory<GossipingEventsPaperTrailViewModel, GossipingEventsPaperTrailState> by hiltMavericksViewModelFactory()
}
