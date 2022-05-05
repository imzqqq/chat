package com.imzqqq.app.features.home.room.detail.timeline.factory

import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventController
import com.imzqqq.app.features.home.room.detail.timeline.helper.TimelineEventsGroup
import org.matrix.android.sdk.api.session.room.timeline.TimelineEvent

data class TimelineItemFactoryParams(
        val event: TimelineEvent,
        val prevEvent: TimelineEvent? = null,
        val nextEvent: TimelineEvent? = null,
        val nextDisplayableEvent: TimelineEvent? = null,
        val partialState: TimelineEventController.PartialState = TimelineEventController.PartialState(),
        val lastSentEventIdWithoutReadReceipts: String? = null,
        val callback: TimelineEventController.Callback? = null,
        val eventsGroup: TimelineEventsGroup? = null
) {

    val highlightedEventId: String?
        get() = partialState.highlightedEventId

    val isHighlighted = highlightedEventId == event.eventId
}
