package com.imzqqq.app.features.home.room.list

import javax.inject.Inject

class RoomSummaryPagedControllerFactory @Inject constructor(
        private val roomSummaryItemFactory: RoomSummaryItemFactory
) {

    fun createRoomSummaryPagedController(): RoomSummaryPagedController {
        return RoomSummaryPagedController(roomSummaryItemFactory)
    }

    fun createRoomSummaryListController(): RoomSummaryListController {
        return RoomSummaryListController(roomSummaryItemFactory)
    }

    fun createSuggestedRoomListController(): SuggestedRoomListController {
        return SuggestedRoomListController(roomSummaryItemFactory)
    }
}
