package com.imzqqq.app.features.home.room.detail.timeline.action

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.core.extensions.canReact
import com.imzqqq.app.features.home.room.detail.timeline.item.MessageInformationData
import org.matrix.android.sdk.api.session.room.send.SendState
import org.matrix.android.sdk.api.session.room.timeline.TimelineEvent

/**
 * Quick reactions state
 */
data class ToggleState(
        val reaction: String,
        val isSelected: Boolean
)

data class ActionPermissions(
        val canSendMessage: Boolean = false,
        val canReact: Boolean = false,
        val canRedact: Boolean = false
)

data class MessageActionState(
        val roomId: String,
        val eventId: String,
        val informationData: MessageInformationData,
        val timelineEvent: Async<TimelineEvent> = Uninitialized,
        val messageBody: CharSequence = "",
        // For quick reactions
        val quickStates: Async<List<ToggleState>> = Uninitialized,
        // For actions
        val actions: List<EventSharedAction> = emptyList(),
        val expendedReportContentMenu: Boolean = false,
        val actionPermissions: ActionPermissions = ActionPermissions()
) : MavericksState {

    constructor(args: TimelineEventFragmentArgs) : this(roomId = args.roomId, eventId = args.eventId, informationData = args.informationData)

    fun senderName(): String = informationData.memberName?.toString() ?: ""

    fun canReact() = timelineEvent()?.canReact() == true && actionPermissions.canReact

    fun sendState(): SendState? = timelineEvent()?.root?.sendState
}
