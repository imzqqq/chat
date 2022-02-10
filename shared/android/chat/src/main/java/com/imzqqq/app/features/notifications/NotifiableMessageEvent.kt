package com.imzqqq.app.features.notifications

import android.net.Uri
import org.matrix.android.sdk.api.session.events.model.EventType

data class NotifiableMessageEvent(
        override val eventId: String,
        override val editedEventId: String?,
        override val canBeReplaced: Boolean,
        val noisy: Boolean,
        val timestamp: Long,
        val senderName: String?,
        val senderId: String?,
        val body: String?,
        val imageUri: Uri?,
        val roomId: String,
        val roomName: String?,
        val roomIsDirect: Boolean = false,
        val roomAvatarPath: String? = null,
        val senderAvatarPath: String? = null,
        val matrixID: String? = null,
        val soundName: String? = null,
        // This is used for >N notification, as the result of a smart reply
        val outGoingMessage: Boolean = false,
        val outGoingMessageFailed: Boolean = false,
        override val isRedacted: Boolean = false
) : NotifiableEvent {

    val type: String = EventType.MESSAGE
    val description: String = body ?: ""
    val title: String = senderName ?: ""
}
