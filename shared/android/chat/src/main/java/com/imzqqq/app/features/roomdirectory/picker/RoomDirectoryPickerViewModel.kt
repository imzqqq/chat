package com.imzqqq.app.features.roomdirectory.picker

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.R
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.ui.UiStateRepository
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.room.model.roomdirectory.PublicRoomsParams

class RoomDirectoryPickerViewModel @AssistedInject constructor(
        @Assisted initialState: RoomDirectoryPickerViewState,
        private val session: Session,
        private val uiStateRepository: UiStateRepository,
        private val stringProvider: StringProvider,
        private val roomDirectoryListCreator: RoomDirectoryListCreator
) : VectorViewModel<RoomDirectoryPickerViewState, RoomDirectoryPickerAction, EmptyViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<RoomDirectoryPickerViewModel, RoomDirectoryPickerViewState> {
        override fun create(initialState: RoomDirectoryPickerViewState): RoomDirectoryPickerViewModel
    }

    companion object : MavericksViewModelFactory<RoomDirectoryPickerViewModel, RoomDirectoryPickerViewState> by hiltMavericksViewModelFactory()

    init {
        observeAndCompute()
        load()
        loadCustomRoomDirectoryHomeservers()
    }

    private fun observeAndCompute() {
        onEach(
                RoomDirectoryPickerViewState::asyncThirdPartyRequest,
                RoomDirectoryPickerViewState::customHomeservers
        ) { async, custom ->
            async()?.let {
                setState {
                    copy(directories = roomDirectoryListCreator.computeDirectories(it, custom))
                }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState {
                copy(asyncThirdPartyRequest = Loading())
            }
            try {
                val thirdPartyProtocols = session.thirdPartyService().getThirdPartyProtocols()
                setState {
                    copy(asyncThirdPartyRequest = Success(thirdPartyProtocols))
                }
            } catch (failure: Throwable) {
                setState {
                    copy(asyncThirdPartyRequest = Fail(failure))
                }
            }
        }
    }

    private fun loadCustomRoomDirectoryHomeservers() {
        setState {
            copy(
                    customHomeservers = uiStateRepository.getCustomRoomDirectoryHomeservers(session.sessionId)
            )
        }
    }

    override fun handle(action: RoomDirectoryPickerAction) {
        when (action) {
            RoomDirectoryPickerAction.Retry           -> load()
            RoomDirectoryPickerAction.EnterEditMode   -> handleEnterEditMode()
            RoomDirectoryPickerAction.ExitEditMode    -> handleExitEditMode()
            is RoomDirectoryPickerAction.SetServerUrl -> handleSetServerUrl(action)
            RoomDirectoryPickerAction.Submit          -> handleSubmit()
            is RoomDirectoryPickerAction.RemoveServer -> handleRemoveServer(action)
        }.exhaustive
    }

    private fun handleEnterEditMode() {
        setState {
            copy(
                    inEditMode = true,
                    enteredServer = "",
                    addServerAsync = Uninitialized
            )
        }
    }

    private fun handleExitEditMode() {
        setState {
            copy(
                    inEditMode = false,
                    enteredServer = "",
                    addServerAsync = Uninitialized
            )
        }
    }

    private fun handleSetServerUrl(action: RoomDirectoryPickerAction.SetServerUrl) {
        setState {
            copy(
                    enteredServer = action.url
            )
        }
    }

    private fun handleSubmit() = withState { state ->
        // First avoid duplicate
        val enteredServer = state.enteredServer

        val existingServerList = state.directories.map { it.serverName }

        if (enteredServer in existingServerList) {
            setState {
                copy(addServerAsync = Fail(Throwable(stringProvider.getString(R.string.directory_add_a_new_server_error_already_added))))
            }
            return@withState
        }

        viewModelScope.launch {
            setState {
                copy(addServerAsync = Loading())
            }
            try {
                session.getPublicRooms(
                        server = enteredServer,
                        publicRoomsParams = PublicRoomsParams(limit = 1)
                )
                // Success, let add the server to our local repository, and update the state
                val newSet = uiStateRepository.getCustomRoomDirectoryHomeservers(session.sessionId) + enteredServer
                uiStateRepository.setCustomRoomDirectoryHomeservers(session.sessionId, newSet)
                setState {
                    copy(
                            inEditMode = false,
                            enteredServer = "",
                            addServerAsync = Uninitialized,
                            customHomeservers = newSet
                    )
                }
            } catch (failure: Throwable) {
                setState {
                    copy(addServerAsync = Fail(failure))
                }
            }
        }
    }

    private fun handleRemoveServer(action: RoomDirectoryPickerAction.RemoveServer) {
        val newSet = uiStateRepository.getCustomRoomDirectoryHomeservers(session.sessionId) - action.roomDirectoryServer.serverName
        uiStateRepository.setCustomRoomDirectoryHomeservers(session.sessionId, newSet)
        setState {
            copy(
                    customHomeservers = newSet
            )
        }
    }
}
