package com.imzqqq.app.core.extensions

import org.matrix.android.sdk.api.session.events.model.EventType
import org.matrix.android.sdk.api.session.room.send.SendState
import org.matrix.android.sdk.api.session.room.timeline.TimelineEvent

fun TimelineEvent.canReact(): Boolean {
    // Only event of type EventType.MESSAGE or EventType.STICKER are supported for the moment
    return root.getClearType() in listOf(EventType.MESSAGE, EventType.STICKER) &&
            root.sendState == SendState.SYNCED &&
            !root.isRedacted()
}
