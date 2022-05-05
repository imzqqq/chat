package com.imzqqq.app.features.notifications

import java.io.Serializable

/**
 * Parent interface for all events which can be displayed as a Notification
 */
sealed interface NotifiableEvent : Serializable {
    val eventId: String
    val editedEventId: String?

    // Used to know if event should be replaced with the one coming from eventstream
    val canBeReplaced: Boolean
    val isRedacted: Boolean
}
