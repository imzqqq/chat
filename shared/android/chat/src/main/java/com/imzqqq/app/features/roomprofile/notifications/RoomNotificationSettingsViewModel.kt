package com.imzqqq.app.features.roomprofile.notifications

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.VectorViewModel
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.flow.flow
import org.matrix.android.sdk.flow.unwrap

class RoomNotificationSettingsViewModel @AssistedInject constructor(
        @Assisted initialState: RoomNotificationSettingsViewState,
        session: Session
) : VectorViewModel<RoomNotificationSettingsViewState, RoomNotificationSettingsAction, RoomNotificationSettingsViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<RoomNotificationSettingsViewModel, RoomNotificationSettingsViewState> {
       override fun create(initialState: RoomNotificationSettingsViewState): RoomNotificationSettingsViewModel
    }

    companion object : MavericksViewModelFactory<RoomNotificationSettingsViewModel, RoomNotificationSettingsViewState> by hiltMavericksViewModelFactory()

    private val room = session.getRoom(initialState.roomId)!!

    init {
        observeSummary()
        observeNotificationState()
    }

    private fun observeSummary() {
        room.flow().liveRoomSummary()
                .unwrap()
                .execute { async ->
                    copy(roomSummary = async)
                }
    }

    private fun observeNotificationState() {
        room.flow()
                .liveNotificationState()
                .execute {
                    copy(notificationState = it)
                }
    }

    override fun handle(action: RoomNotificationSettingsAction) {
        when (action) {
            is RoomNotificationSettingsAction.SelectNotificationState -> handleSelectNotificationState(action)
        }
    }

    private fun handleSelectNotificationState(action: RoomNotificationSettingsAction.SelectNotificationState) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {  room.setRoomNotificationState(action.notificationState) }
                    .fold(
                            {
                                setState {
                                    copy(isLoading = false, notificationState = Success(action.notificationState))
                                }
                            },
                            {
                                setState {
                                    copy(isLoading = false)
                                }
                                _viewEvents.post(RoomNotificationSettingsViewEvents.Failure(it))
                            }
                    )
        }
    }
}
