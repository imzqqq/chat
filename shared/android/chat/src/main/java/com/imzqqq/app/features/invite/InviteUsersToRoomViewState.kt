package com.imzqqq.app.features.invite

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized

data class InviteUsersToRoomViewState(
        val roomId: String,
        val inviteState: Async<Unit> = Uninitialized
) : MavericksState {

    constructor(args: InviteUsersToRoomArgs) : this(roomId = args.roomId)
}
