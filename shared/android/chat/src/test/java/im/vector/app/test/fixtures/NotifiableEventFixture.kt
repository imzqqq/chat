package com.imzqqq.app.test.fixtures

import com.imzqqq.app.features.notifications.InviteNotifiableEvent
import com.imzqqq.app.features.notifications.NotifiableMessageEvent
import com.imzqqq.app.features.notifications.SimpleNotifiableEvent

fun aSimpleNotifiableEvent(
        eventId: String = "simple-event-id",
        type: String? = null,
        isRedacted: Boolean = false,
        canBeReplaced: Boolean = false,
        editedEventId: String? = null
) = SimpleNotifiableEvent(
        matrixID = null,
        eventId = eventId,
        editedEventId = editedEventId,
        noisy = false,
        title = "title",
        description = "description",
        type = type,
        timestamp = 0,
        soundName = null,
        canBeReplaced = canBeReplaced,
        isRedacted = isRedacted
)

fun anInviteNotifiableEvent(
        roomId: String = "an-invite-room-id",
        eventId: String = "invite-event-id",
        isRedacted: Boolean = false
) = InviteNotifiableEvent(
        matrixID = null,
        eventId = eventId,
        roomId = roomId,
        roomName = "a room name",
        editedEventId = null,
        noisy = false,
        title = "title",
        description = "description",
        type = null,
        timestamp = 0,
        soundName = null,
        canBeReplaced = false,
        isRedacted = isRedacted
)

fun aNotifiableMessageEvent(
        eventId: String = "a-message-event-id",
        roomId: String = "a-message-room-id",
        isRedacted: Boolean = false
) = NotifiableMessageEvent(
        eventId = eventId,
        editedEventId = null,
        noisy = false,
        timestamp = 0,
        senderName = "sender-name",
        senderId = "sending-id",
        body = "message-body",
        roomId = roomId,
        roomName = "room-name",
        roomIsDirect = false,
        canBeReplaced = false,
        isRedacted = isRedacted,
        imageUri = null
)
