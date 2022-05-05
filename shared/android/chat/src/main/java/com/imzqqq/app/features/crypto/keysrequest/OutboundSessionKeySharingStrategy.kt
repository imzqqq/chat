package com.imzqqq.app.features.crypto.keysrequest

enum class OutboundSessionKeySharingStrategy {
    /**
     * Keys will be sent for the first time when the first message is sent
     */
    WhenSendingEvent,

    /**
     * Keys will be sent for the first time when the timeline displayed
     */
    WhenEnteringRoom,

    /**
     * Keys will be sent for the first time when a typing started
     */
    WhenTyping
}
