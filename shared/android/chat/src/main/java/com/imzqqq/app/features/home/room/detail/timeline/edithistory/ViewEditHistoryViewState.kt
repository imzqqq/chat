package com.imzqqq.app.features.home.room.detail.timeline.edithistory

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.features.home.room.detail.timeline.action.TimelineEventFragmentArgs
import org.matrix.android.sdk.api.session.events.model.Event

data class ViewEditHistoryViewState(
        val eventId: String,
        val roomId: String,
        val isOriginalAReply: Boolean = false,
        val editList: Async<List<Event>> = Uninitialized) :
    MavericksState {

    constructor(args: TimelineEventFragmentArgs) : this(roomId = args.roomId, eventId = args.eventId)
}
