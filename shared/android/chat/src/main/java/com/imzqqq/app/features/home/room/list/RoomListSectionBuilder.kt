package com.imzqqq.app.features.home.room.list

import com.imzqqq.app.features.home.RoomListDisplayMode

interface RoomListSectionBuilder {
    fun buildSections(mode: RoomListDisplayMode): List<RoomsSection>
}
