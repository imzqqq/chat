package com.imzqqq.app.features.roomdirectory.createroom

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.room.model.RoomJoinRules
import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class CreateRoomViewState(
        val avatarUri: Uri? = null,
        val roomName: String = "",
        val roomTopic: String = "",
        val roomJoinRules: RoomJoinRules = RoomJoinRules.INVITE,
        val isEncrypted: Boolean? = null,
        val defaultEncrypted: Map<RoomJoinRules, Boolean> = emptyMap(),
        val showAdvanced: Boolean = false,
        val disableFederation: Boolean = false,
        val homeServerName: String = "",
        val hsAdminHasDisabledE2E: Boolean = false,
        val asyncCreateRoomRequest: Async<String> = Uninitialized,
        val parentSpaceId: String?,
        val parentSpaceSummary: RoomSummary? = null,
        val supportsRestricted: Boolean = false,
        val aliasLocalPart: String? = null,
        val isSubSpace: Boolean = false
) : MavericksState {

    constructor(args: CreateRoomArgs) : this(
            roomName = args.initialName,
            parentSpaceId = args.parentSpaceId,
            isSubSpace = args.isSpace
    )

    /**
     * Return true if there is not important input from user
     */
    fun isEmpty() = avatarUri == null &&
            roomName.isEmpty() &&
            roomTopic.isEmpty() &&
            aliasLocalPart.isNullOrEmpty()
}
