package com.imzqqq.app.features.room

import com.airbnb.mvrx.MavericksState
import com.imzqqq.app.features.home.room.detail.RoomDetailArgs
import com.imzqqq.app.features.roommemberprofile.RoomMemberProfileArgs
import com.imzqqq.app.features.roomprofile.RoomProfileArgs

data class RequireActiveMembershipViewState(
        val roomId: String? = null
) : MavericksState {

    constructor(args: RoomDetailArgs) : this(roomId = args.roomId)

    constructor(args: RoomProfileArgs) : this(roomId = args.roomId)

    constructor(args: RoomMemberProfileArgs) : this(roomId = args.roomId)
}
