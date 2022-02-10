package com.imzqqq.app.features.createdirect

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized

data class CreateDirectRoomViewState(
        val createAndInviteState: Async<String> = Uninitialized
) : MavericksState
