package com.imzqqq.app.features.home.room.list

class SuggestedRoomListController(
        private val roomSummaryItemFactory: RoomSummaryItemFactory
) : CollapsableTypedEpoxyController<SuggestedRoomInfo>() {

    var listener: RoomListListener? = null

    override fun buildModels(data: SuggestedRoomInfo?) {
        data?.rooms?.forEach { info ->
            add(roomSummaryItemFactory.createSuggestion(info, data.joinEcho, listener))
        }
    }
}
