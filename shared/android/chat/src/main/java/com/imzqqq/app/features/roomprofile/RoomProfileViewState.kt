package com.imzqqq.app.features.roomprofile

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.model.create.RoomCreateContent

data class RoomProfileViewState(
        val roomId: String,
        val roomSummary: Async<RoomSummary> = Uninitialized,
        val roomCreateContent: Async<RoomCreateContent> = Uninitialized,
        val bannedMembership: Async<List<RoomMemberSummary>> = Uninitialized,
        val actionPermissions: ActionPermissions = ActionPermissions(),
        val isLoading: Boolean = false,
        val isUsingUnstableRoomVersion: Boolean = false,
        val recommendedRoomVersion: String? = null,
        val canUpgradeRoom: Boolean = false,
        val isTombstoned: Boolean = false
) : MavericksState {

    constructor(args: RoomProfileArgs) : this(roomId = args.roomId)

    data class ActionPermissions(
            val canEnableEncryption: Boolean = false
    )
}
