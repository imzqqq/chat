package com.imzqqq.app.features.invite

import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.R
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.userdirectory.PendingSelection
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session

class InviteUsersToRoomViewModel @AssistedInject constructor(@Assisted
                                                             initialState: InviteUsersToRoomViewState,
                                                             session: Session,
                                                             val stringProvider: StringProvider) :
    VectorViewModel<InviteUsersToRoomViewState, InviteUsersToRoomAction, InviteUsersToRoomViewEvents>(initialState) {

    private val room = session.getRoom(initialState.roomId)!!

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<InviteUsersToRoomViewModel, InviteUsersToRoomViewState> {
        override fun create(initialState: InviteUsersToRoomViewState): InviteUsersToRoomViewModel
    }

    companion object : MavericksViewModelFactory<InviteUsersToRoomViewModel, InviteUsersToRoomViewState> by hiltMavericksViewModelFactory()

    override fun handle(action: InviteUsersToRoomAction) {
        when (action) {
            is InviteUsersToRoomAction.InviteSelectedUsers -> inviteUsersToRoom(action.selections)
        }
    }

    private fun inviteUsersToRoom(selections: Set<PendingSelection>) {
        viewModelScope.launch {
            _viewEvents.post(InviteUsersToRoomViewEvents.Loading)
            selections.asFlow()
                    .map { user ->
                        when (user) {
                            is PendingSelection.UserPendingSelection     -> room.invite(user.user.userId, null)
                            is PendingSelection.ThreePidPendingSelection -> room.invite3pid(user.threePid)
                        }
                    }
                    .catch { cause ->
                        _viewEvents.post(InviteUsersToRoomViewEvents.Failure(cause))
                    }
                    .collect {
                        val successMessage = when (selections.size) {
                            1    -> stringProvider.getString(R.string.invitation_sent_to_one_user,
                                    selections.first().getBestName())
                            2    -> stringProvider.getString(R.string.invitations_sent_to_two_users,
                                    selections.first().getBestName(),
                                    selections.last().getBestName())
                            else -> stringProvider.getQuantityString(R.plurals.invitations_sent_to_one_and_more_users,
                                    selections.size - 1,
                                    selections.first().getBestName(),
                                    selections.size - 1)
                        }
                        _viewEvents.post(InviteUsersToRoomViewEvents.Success(successMessage))
                    }
        }
    }

    fun getUserIdsOfRoomMembers(): Set<String> {
        return room.roomSummary()?.otherMemberIds?.toSet().orEmpty()
    }
}
