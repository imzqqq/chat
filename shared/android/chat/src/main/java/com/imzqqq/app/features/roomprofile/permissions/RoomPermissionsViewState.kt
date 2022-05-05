package com.imzqqq.app.features.roomprofile.permissions

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.features.roomprofile.RoomProfileArgs
import org.matrix.android.sdk.api.session.room.model.PowerLevelsContent
import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class RoomPermissionsViewState(
        val roomId: String,
        val roomSummary: Async<RoomSummary> = Uninitialized,
        val actionPermissions: ActionPermissions = ActionPermissions(),
        val showAdvancedPermissions: Boolean = false,
        val currentPowerLevelsContent: Async<PowerLevelsContent> = Uninitialized,
        val isLoading: Boolean = false
) : MavericksState {

    constructor(args: RoomProfileArgs) : this(roomId = args.roomId)

    data class ActionPermissions(
            val canChangePowerLevels: Boolean = false
    )
}
