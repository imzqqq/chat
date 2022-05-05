package com.imzqqq.app.features.home.room.detail.upgrade

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.session.coroutineScope
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session

class MigrateRoomViewModel @AssistedInject constructor(
        @Assisted initialState: MigrateRoomViewState,
        private val session: Session,
        private val upgradeRoomViewModelTask: UpgradeRoomViewModelTask) :
    VectorViewModel<MigrateRoomViewState, MigrateRoomAction, EmptyViewEvents>(initialState) {

    init {
        val room = session.getRoom(initialState.roomId)
        val summary = session.getRoomSummary(initialState.roomId)
        setState {
            copy(
                    currentVersion = room?.getRoomVersion(),
                    isPublic = summary?.isPublic ?: false,
                    otherMemberCount = summary?.otherMemberIds?.count() ?: 0,
                    knownParents = summary?.flattenParentIds ?: emptyList()
            )
        }
    }

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<MigrateRoomViewModel, MigrateRoomViewState> {
        override fun create(initialState: MigrateRoomViewState): MigrateRoomViewModel
    }

    companion object : MavericksViewModelFactory<MigrateRoomViewModel, MigrateRoomViewState> by hiltMavericksViewModelFactory()

    override fun handle(action: MigrateRoomAction) {
        when (action) {
            is MigrateRoomAction.SetAutoInvite             -> {
                setState {
                    copy(shouldIssueInvites = action.autoInvite)
                }
            }
            is MigrateRoomAction.SetUpdateKnownParentSpace -> {
                setState {
                    copy(shouldUpdateKnownParents = action.update)
                }
            }
            MigrateRoomAction.UpgradeRoom                  -> {
                handleUpgradeRoom()
            }
        }
    }

    private fun handleUpgradeRoom() = withState { state ->
        val summary = session.getRoomSummary(state.roomId)
        setState {
            copy(upgradingStatus = Loading())
        }
        session.coroutineScope.launch {
            val userToInvite = if (state.autoMigrateMembersAndParents) {
                summary?.otherMemberIds?.takeIf { !state.isPublic }
            } else {
                summary?.otherMemberIds?.takeIf { state.shouldIssueInvites }
            }.orEmpty()

            val parentSpaceToUpdate = if (state.autoMigrateMembersAndParents) {
                summary?.flattenParentIds
            } else {
                summary?.flattenParentIds?.takeIf { state.shouldUpdateKnownParents }
            }.orEmpty()

            val result = upgradeRoomViewModelTask.execute(UpgradeRoomViewModelTask.Params(
                    roomId = state.roomId,
                    newVersion = state.newVersion,
                    userIdsToAutoInvite = userToInvite,
                    parentSpaceToUpdate = parentSpaceToUpdate,
                    progressReporter = { indeterminate, progress, total ->
                        setState {
                            copy(
                                    upgradingProgress = progress,
                                    upgradingProgressTotal = total,
                                    upgradingProgressIndeterminate = indeterminate
                            )
                        }
                    }
            ))

            setState {
                copy(upgradingStatus = Success(result))
            }
        }
    }
}
