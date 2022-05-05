package com.imzqqq.app.features.notifications

import com.imzqqq.app.features.invite.AutoAcceptInvites
import com.imzqqq.app.features.notifications.ProcessedEvent.Type.KEEP
import com.imzqqq.app.features.notifications.ProcessedEvent.Type.REMOVE
import org.matrix.android.sdk.api.session.events.model.EventType
import javax.inject.Inject

private typealias ProcessedEvents = List<ProcessedEvent<NotifiableEvent>>

class NotifiableEventProcessor @Inject constructor(
        private val outdatedDetector: OutdatedEventDetector,
        private val autoAcceptInvites: AutoAcceptInvites
) {

    fun process(queuedEvents: List<NotifiableEvent>, currentRoomId: String?, renderedEvents: ProcessedEvents): ProcessedEvents {
        val processedEvents = queuedEvents.map {
            val type = when (it) {
                is InviteNotifiableEvent  -> if (autoAcceptInvites.hideInvites) REMOVE else KEEP
                is NotifiableMessageEvent -> if (shouldIgnoreMessageEventInRoom(currentRoomId, it.roomId) || outdatedDetector.isMessageOutdated(it)) {
                    REMOVE
                } else KEEP
                is SimpleNotifiableEvent  -> when (it.type) {
                    EventType.REDACTION -> REMOVE
                    else                -> KEEP
                }
            }
            ProcessedEvent(type, it)
        }

        val removedEventsDiff = renderedEvents.filter { renderedEvent ->
            queuedEvents.none { it.eventId == renderedEvent.event.eventId }
        }.map { ProcessedEvent(REMOVE, it.event) }

        return removedEventsDiff + processedEvents
    }

    private fun shouldIgnoreMessageEventInRoom(currentRoomId: String?, roomId: String?): Boolean {
        return currentRoomId != null && roomId == currentRoomId
    }
}
