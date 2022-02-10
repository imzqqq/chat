package com.imzqqq.app.features.home.room.detail.timeline.item

interface ItemWithEvents {
    /**
     * Returns the eventIds associated with the EventItem.
     * Will generally get only one, but it handles the merged items.
     */
    fun getEventIds(): List<String>

    fun canAppendReadMarker(): Boolean = true

    fun isVisible(): Boolean = true

    /**
     * Returns false if you want epoxy controller to rebuild the event each time a built is triggered
     */
    fun isCacheable(): Boolean = true
}
