package com.imzqqq.app.features.spaces.leave

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.AppStateHandler
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import org.matrix.android.sdk.api.query.ActiveSpaceFilter
import org.matrix.android.sdk.api.query.RoomCategoryFilter
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import org.matrix.android.sdk.flow.flow
import org.matrix.android.sdk.flow.unwrap
import timber.log.Timber

class SpaceLeaveAdvancedViewModel @AssistedInject constructor(
        @Assisted val initialState: SpaceLeaveAdvanceViewState,
        private val session: Session,
        private val appStateHandler: AppStateHandler
) : VectorViewModel<SpaceLeaveAdvanceViewState, SpaceLeaveAdvanceViewAction, EmptyViewEvents>(initialState) {

    override fun handle(action: SpaceLeaveAdvanceViewAction) = withState { state ->
        when (action) {
            is SpaceLeaveAdvanceViewAction.ToggleSelection -> {
                val existing = state.selectedRooms.toMutableList()
                if (existing.contains(action.roomId)) {
                    existing.remove(action.roomId)
                } else {
                    existing.add(action.roomId)
                }
                setState {
                    copy(
                            selectedRooms = existing.toImmutableList()
                    )
                }
            }
            is SpaceLeaveAdvanceViewAction.UpdateFilter    -> {
                setState { copy(currentFilter = action.filter) }
            }
            SpaceLeaveAdvanceViewAction.DoLeave            -> {
                setState { copy(leaveState = Loading()) }
                viewModelScope.launch {
                    try {
                        state.selectedRooms.forEach {
                            try {
                                session.getRoom(it)?.leave(null)
                            } catch (failure: Throwable) {
                                // silently ignore?
                                Timber.e(failure, "Fail to leave sub rooms/spaces")
                            }
                        }

                        session.getRoom(initialState.spaceId)?.leave(null)
                        // We observe the membership and to dismiss when we have remote echo of leaving
                    } catch (failure: Throwable) {
                        setState { copy(leaveState = Fail(failure)) }
                    }
                }
            }
            SpaceLeaveAdvanceViewAction.ClearError         -> {
                setState { copy(leaveState = Uninitialized) }
            }
        }
    }

    init {
        val spaceSummary = session.getRoomSummary(initialState.spaceId)
        setState { copy(spaceSummary = spaceSummary) }
        session.getRoom(initialState.spaceId)?.let { room ->
            room.flow().liveRoomSummary()
                    .unwrap()
                    .onEach {
                        if (it.membership == Membership.LEAVE) {
                            setState { copy(leaveState = Success(Unit)) }
                            if (appStateHandler.safeActiveSpaceId() == initialState.spaceId) {
                                // switch to home?
                                appStateHandler.setCurrentSpace(null, session)
                            }
                        }
                    }.launchIn(viewModelScope)
        }

        viewModelScope.launch {
            val children = session.getRoomSummaries(
                    roomSummaryQueryParams {
                        includeType = null
                        memberships = listOf(Membership.JOIN)
                        activeSpaceFilter = ActiveSpaceFilter.ActiveSpace(initialState.spaceId)
                        roomCategoryFilter = RoomCategoryFilter.ONLY_ROOMS
                    }
            )

            setState {
                copy(allChildren = Success(children))
            }
        }
    }

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<SpaceLeaveAdvancedViewModel, SpaceLeaveAdvanceViewState> {
        override fun create(initialState: SpaceLeaveAdvanceViewState): SpaceLeaveAdvancedViewModel
    }

    companion object : MavericksViewModelFactory<SpaceLeaveAdvancedViewModel, SpaceLeaveAdvanceViewState> by hiltMavericksViewModelFactory()
}
