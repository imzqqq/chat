package com.imzqqq.app.features.poll.create

import com.airbnb.mvrx.MavericksState

data class CreatePollViewState(
        val roomId: String,
        val question: String = "",
        val options: List<String> = List(CreatePollViewModel.MIN_OPTIONS_COUNT) { "" },
        val canCreatePoll: Boolean = false,
        val canAddMoreOptions: Boolean = true
) : MavericksState {

    constructor(args: CreatePollArgs) : this(
            roomId = args.roomId
    )
}
