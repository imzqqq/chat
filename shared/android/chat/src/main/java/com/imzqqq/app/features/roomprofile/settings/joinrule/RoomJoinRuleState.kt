package com.imzqqq.app.features.roomprofile.settings.joinrule

import com.imzqqq.app.core.ui.bottomsheet.BottomSheetGenericState
import org.matrix.android.sdk.api.session.room.model.GuestAccess
import org.matrix.android.sdk.api.session.room.model.RoomJoinRules

data class RoomJoinRuleState(
        val currentRoomJoinRule: RoomJoinRules = RoomJoinRules.INVITE,
        val allowedJoinedRules: List<JoinRulesOptionSupport> =
                listOf(RoomJoinRules.INVITE, RoomJoinRules.PUBLIC).map { it.toOption(true) },
        val currentGuestAccess: GuestAccess? = null,
        val isSpace: Boolean = false,
        val parentSpaceName: String?
) : BottomSheetGenericState() {

    constructor(args: RoomJoinRuleBottomSheetArgs) : this(
            currentRoomJoinRule = args.currentRoomJoinRule,
            allowedJoinedRules = args.allowedJoinedRules,
            isSpace = args.isSpace,
            parentSpaceName = args.parentSpaceName
    )
}
