package com.imzqqq.app.features.spaces.invite

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.session.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.extensions.tryOrNull
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.peeking.PeekResult

class SpaceInviteBottomSheetViewModel @AssistedInject constructor(
        @Assisted private val initialState: SpaceInviteBottomSheetState,
        private val session: Session,
        private val errorFormatter: ErrorFormatter
) : VectorViewModel<SpaceInviteBottomSheetState, SpaceInviteBottomSheetAction, SpaceInviteBottomSheetEvents>(initialState) {

    init {
        session.getRoomSummary(initialState.spaceId)?.let { roomSummary ->
            val knownMembers = roomSummary.otherMemberIds.filter {
                session.getExistingDirectRoomWithUser(it) != null
            }.mapNotNull { session.getUser(it) }
            // put one with avatar first, and take 5
            val peopleYouKnow = (knownMembers.filter { it.avatarUrl != null } + knownMembers.filter { it.avatarUrl == null })
                    .take(5)

            setState {
                copy(
                        summary = Success(roomSummary),
                        inviterUser = roomSummary.inviterId?.let { session.getUser(it) }?.let { Success(it) } ?: Uninitialized,
                        peopleYouKnow = Success(peopleYouKnow)
                )
            }
            if (roomSummary.membership == Membership.INVITE) {
                getLatestRoomSummary(roomSummary)
            }
        }
    }

    /**
     * Try to request the room summary api to get more info
     */
    private fun getLatestRoomSummary(roomSummary: RoomSummary) {
        viewModelScope.launch(Dispatchers.IO) {
            val peekResult = tryOrNull { session.peekRoom(roomSummary.roomId) } as? PeekResult.Success ?: return@launch
            setState {
                copy(
                        summary = Success(
                                roomSummary.copy(
                                        joinedMembersCount = peekResult.numJoinedMembers,
                                        // it's also possible that the name/avatar did change since the invite..
                                        // if it's null keep the old one as summary API might not be available
                                        // and peek result could be null for other reasons (not peekable)
                                        avatarUrl = peekResult.avatarUrl ?: roomSummary.avatarUrl,
                                        displayName = peekResult.name ?: roomSummary.displayName,
                                        topic = peekResult.topic ?: roomSummary.topic
                                        // maybe use someMembers field later?
                                )
                        )
                )
            }
        }
    }

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<SpaceInviteBottomSheetViewModel, SpaceInviteBottomSheetState> {
        override fun create(initialState: SpaceInviteBottomSheetState): SpaceInviteBottomSheetViewModel
    }

    companion object : MavericksViewModelFactory<SpaceInviteBottomSheetViewModel, SpaceInviteBottomSheetState> by hiltMavericksViewModelFactory()

    override fun handle(action: SpaceInviteBottomSheetAction) {
        when (action) {
            SpaceInviteBottomSheetAction.DoJoin   -> {
                setState { copy(joinActionState = Loading()) }
                session.coroutineScope.launch(Dispatchers.IO) {
                    try {
                        session.getRoom(initialState.spaceId)?.join()
                        setState { copy(joinActionState = Success(Unit)) }
                    } catch (failure: Throwable) {
                        setState { copy(joinActionState = Fail(failure)) }
                        _viewEvents.post(SpaceInviteBottomSheetEvents.ShowError(errorFormatter.toHumanReadable(failure)))
                    }
                }
            }
            SpaceInviteBottomSheetAction.DoReject -> {
                setState { copy(rejectActionState = Loading()) }
                session.coroutineScope.launch(Dispatchers.IO) {
                    try {
                        session.getRoom(initialState.spaceId)?.leave()
                        setState { copy(rejectActionState = Success(Unit)) }
                    } catch (failure: Throwable) {
                        setState { copy(rejectActionState = Fail(failure)) }
                        _viewEvents.post(SpaceInviteBottomSheetEvents.ShowError(errorFormatter.toHumanReadable(failure)))
                    }
                }
            }
        }
    }
}
