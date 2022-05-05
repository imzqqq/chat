package com.imzqqq.app.features.roomprofile.members

import androidx.annotation.StringRes
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.R
import com.imzqqq.app.core.platform.GenericIdArgs
import com.imzqqq.app.features.roomprofile.RoomProfileArgs
import org.matrix.android.sdk.api.crypto.RoomEncryptionTrustLevel
import org.matrix.android.sdk.api.session.events.model.Event
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary
import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class RoomMemberListViewState(
        val roomId: String,
        val roomSummary: Async<RoomSummary> = Uninitialized,
        val roomMemberSummaries: Async<RoomMemberSummariesWithPower> = Uninitialized,
        val filter: String = "",
        val threePidInvites: Async<List<Event>> = Uninitialized,
        val trustLevelMap: Async<Map<String, RoomEncryptionTrustLevel?>> = Uninitialized,
        val actionsPermissions: ActionPermissions = ActionPermissions()
) : MavericksState {

    constructor(args: RoomProfileArgs) : this(roomId = args.roomId)

    constructor(args: GenericIdArgs) : this(roomId = args.id)
}

data class ActionPermissions(
        val canInvite: Boolean = false,
        val canRevokeThreePidInvite: Boolean = false
)

typealias RoomMemberSummaries = List<Pair<RoomMemberListCategories, List<RoomMemberSummary>>>
typealias RoomMemberSummariesWithPower = List<Pair<RoomMemberListCategories, List<RoomMemberListViewModel.RoomMemberSummaryWithPower>>>

enum class RoomMemberListCategories(@StringRes val titleRes: Int) {
    ADMIN(R.string.room_member_power_level_admins),
    MODERATOR(R.string.room_member_power_level_moderators),
    CUSTOM(R.string.room_member_power_level_custom),
    INVITE(R.string.room_member_power_level_invites),
    USER(R.string.room_member_power_level_users),

    // Singular variants
    SG_ADMIN(R.string.power_level_admin),
    SG_MODERATOR(R.string.power_level_moderator),
    SG_CUSTOM(R.string.power_level_custom_no_value),
    SG_USER(R.string.power_level_default),
    // Header for unified members
    MEMBER(R.string.room_member_power_level_users)
}
