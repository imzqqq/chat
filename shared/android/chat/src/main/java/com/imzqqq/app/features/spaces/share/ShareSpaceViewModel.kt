package com.imzqqq.app.features.spaces.share

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.powerlevel.PowerLevelsFlowFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.matrix.android.sdk.api.extensions.orFalse
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.room.powerlevels.PowerLevelsHelper

class ShareSpaceViewModel @AssistedInject constructor(
        @Assisted private val initialState: ShareSpaceViewState,
        private val session: Session) : VectorViewModel<ShareSpaceViewState, ShareSpaceAction, ShareSpaceViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<ShareSpaceViewModel, ShareSpaceViewState> {
        override fun create(initialState: ShareSpaceViewState): ShareSpaceViewModel
    }

    companion object : MavericksViewModelFactory<ShareSpaceViewModel, ShareSpaceViewState> by hiltMavericksViewModelFactory()

    init {
        val roomSummary = session.getRoomSummary(initialState.spaceId)
        setState {
            copy(
                    spaceSummary = roomSummary?.let { Success(it) } ?: Uninitialized,
                    canShareLink = roomSummary?.isPublic.orFalse()
            )
        }
        observePowerLevel()
    }

    private fun observePowerLevel() {
        val room = session.getRoom(initialState.spaceId) ?: return
        PowerLevelsFlowFactory(room)
                .createFlow()
                .onEach { powerLevelContent ->
                    val powerLevelsHelper = PowerLevelsHelper(powerLevelContent)
                    setState {
                        copy(
                                canInviteByMxId = powerLevelsHelper.isUserAbleToInvite(session.myUserId)
                        )
                    }
                }
                .launchIn(viewModelScope)
    }

    override fun handle(action: ShareSpaceAction) {
        when (action) {
            ShareSpaceAction.InviteByLink -> {
                val roomSummary = session.getRoomSummary(initialState.spaceId)
                val alias = roomSummary?.canonicalAlias
                val permalink = if (alias != null) {
                    session.permalinkService().createPermalink(alias)
                } else {
                    session.permalinkService().createRoomPermalink(initialState.spaceId)
                }
                if (permalink != null) {
                    _viewEvents.post(ShareSpaceViewEvents.ShowInviteByLink(permalink, roomSummary?.name ?: ""))
                }
            }
            ShareSpaceAction.InviteByMxId -> {
                _viewEvents.post(ShareSpaceViewEvents.NavigateToInviteUser(initialState.spaceId))
            }
        }
    }
}
