package com.imzqqq.app.features.home.room.detail.composer

import com.airbnb.mvrx.MavericksState
import com.imzqqq.app.features.home.room.detail.RoomDetailArgs
import org.matrix.android.sdk.api.session.room.timeline.TimelineEvent

/**
 * Describes the current send mode:
 * REGULAR: sends the text as a regular message
 * QUOTE: User is currently quoting a message
 * EDIT: User is currently editing an existing message
 *
 * Depending on the state the bottom toolbar will change (icons/preview/actions...)
 */
sealed class SendMode(open val text: String) {
    data class REGULAR(
            override val text: String,
            val fromSharing: Boolean,
            // This is necessary for forcing refresh on selectSubscribe
            private val ts: Long = System.currentTimeMillis()
    ) : SendMode(text)

    data class QUOTE(val timelineEvent: TimelineEvent, override val text: String) : SendMode(text)
    data class EDIT(val timelineEvent: TimelineEvent, override val text: String) : SendMode(text)
    data class REPLY(val timelineEvent: TimelineEvent, override val text: String) : SendMode(text)
}

data class TextComposerViewState(
        val roomId: String,
        val canSendMessage: Boolean = true,
        val isVoiceRecording: Boolean = false,
        val isSendButtonActive: Boolean = false,
        val isSendButtonVisible: Boolean = false,
        val sendMode: SendMode = SendMode.REGULAR("", false)
) : MavericksState {

    val isComposerVisible = canSendMessage && !isVoiceRecording
    val isVoiceMessageRecorderVisible = canSendMessage && !isSendButtonVisible

    constructor(args: RoomDetailArgs) : this(roomId = args.roomId)
}
