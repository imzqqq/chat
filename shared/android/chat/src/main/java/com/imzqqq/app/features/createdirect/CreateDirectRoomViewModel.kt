package com.imzqqq.app.features.createdirect

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.mvrx.runCatchingToAsync
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.raw.wellknown.getElementWellknown
import com.imzqqq.app.features.raw.wellknown.isE2EByDefault
import com.imzqqq.app.features.userdirectory.PendingSelection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.raw.RawService
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.room.model.create.CreateRoomParams

class CreateDirectRoomViewModel @AssistedInject constructor(@Assisted
                                                            initialState: CreateDirectRoomViewState,
                                                            private val rawService: RawService,
                                                            val session: Session) :
    VectorViewModel<CreateDirectRoomViewState, CreateDirectRoomAction, CreateDirectRoomViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<CreateDirectRoomViewModel, CreateDirectRoomViewState> {
        override fun create(initialState: CreateDirectRoomViewState): CreateDirectRoomViewModel
    }

    companion object : MavericksViewModelFactory<CreateDirectRoomViewModel, CreateDirectRoomViewState> by hiltMavericksViewModelFactory()

    override fun handle(action: CreateDirectRoomAction) {
        when (action) {
            is CreateDirectRoomAction.CreateRoomAndInviteSelectedUsers -> onSubmitInvitees(action)
        }.exhaustive
    }

    /**
     * If users already have a DM room then navigate to it instead of creating a new room.
     */
    private fun onSubmitInvitees(action: CreateDirectRoomAction.CreateRoomAndInviteSelectedUsers) {
        val existingRoomId = action.selections.singleOrNull()?.getMxId()?.let { userId ->
            session.getExistingDirectRoomWithUser(userId)
        }
        if (existingRoomId != null) {
            // Do not create a new DM, just tell that the creation is successful by passing the existing roomId
            setState {
                copy(createAndInviteState = Success(existingRoomId))
            }
        } else {
            // Create the DM
            createRoomAndInviteSelectedUsers(action.selections)
        }
    }

    private fun createRoomAndInviteSelectedUsers(selections: Set<PendingSelection>) {
        setState { copy(createAndInviteState = Loading()) }

        viewModelScope.launch(Dispatchers.IO) {
            val adminE2EByDefault = rawService.getElementWellknown(session.sessionParams)
                    ?.isE2EByDefault()
                    ?: true

            val roomParams = CreateRoomParams()
                    .apply {
                        selections.forEach {
                            when (it) {
                                is PendingSelection.UserPendingSelection     -> invitedUserIds.add(it.user.userId)
                                is PendingSelection.ThreePidPendingSelection -> invite3pids.add(it.threePid)
                            }.exhaustive
                        }
                        setDirectMessage()
                        enableEncryptionIfInvitedUsersSupportIt = adminE2EByDefault
                    }

            val result = runCatchingToAsync {
                session.createRoom(roomParams)
            }

            setState {
                copy(
                        createAndInviteState = result
                )
            }
        }
    }
}
