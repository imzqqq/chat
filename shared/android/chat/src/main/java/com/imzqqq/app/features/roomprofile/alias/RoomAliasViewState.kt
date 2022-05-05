package com.imzqqq.app.features.roomprofile.alias

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.features.roomprofile.RoomProfileArgs
import org.matrix.android.sdk.api.session.room.model.RoomDirectoryVisibility
import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class RoomAliasViewState(
        val roomId: String,
        val homeServerName: String = "",
        val roomSummary: Async<RoomSummary> = Uninitialized,
        val actionPermissions: ActionPermissions = ActionPermissions(),
        val roomDirectoryVisibility: Async<RoomDirectoryVisibility> = Uninitialized,
        val isLoading: Boolean = false,
        val canonicalAlias: String? = null,
        val alternativeAliases: List<String> = emptyList(),
        val publishManuallyState: AddAliasState = AddAliasState.Hidden,
        val localAliases: Async<List<String>> = Uninitialized,
        val newLocalAliasState: AddAliasState = AddAliasState.Closed
) : MavericksState {

    constructor(args: RoomProfileArgs) : this(roomId = args.roomId)

    val allPublishedAliases: List<String>
        get() = (alternativeAliases + listOfNotNull(canonicalAlias)).distinct()

    data class ActionPermissions(
            val canChangeCanonicalAlias: Boolean = false
    )

    sealed class AddAliasState {
        object Hidden : AddAliasState()
        object Closed : AddAliasState()
        data class Editing(val value: String, val asyncRequest: Async<Unit> = Uninitialized) : AddAliasState()
    }
}
