package com.imzqqq.app.features.home.room.list

import org.matrix.android.sdk.api.session.room.model.RoomSummary

class RoomSummaryListController(
        private val roomSummaryItemFactory: RoomSummaryItemFactory
) : CollapsableTypedEpoxyController<List<RoomSummary>>() {

    var listener: RoomListListener? = null

    override fun buildModels(data: List<RoomSummary>?) {
        data?.forEach {
            add(roomSummaryItemFactory.create(it, emptyMap(), emptySet(), listener))
        }
    }
}
