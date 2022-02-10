package com.imzqqq.app.features.notifications

data class SimpleNotifiableEvent(
        val matrixID: String?,
        override val eventId: String,
        override val editedEventId: String?,
        val noisy: Boolean,
        val title: String,
        val description: String,
        val type: String?,
        val timestamp: Long,
        val soundName: String?,
        override var canBeReplaced: Boolean,
        override val isRedacted: Boolean = false
) : NotifiableEvent
