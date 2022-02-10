package com.imzqqq.app.features.home.room.list

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.helpFooterItem
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.resources.UserPreferencesProvider
import com.imzqqq.app.features.home.RoomListDisplayMode
import com.imzqqq.app.features.home.room.filtered.FilteredRoomFooterItem
import com.imzqqq.app.features.home.room.filtered.filteredRoomFooterItem
import com.imzqqq.app.space
import javax.inject.Inject

class RoomListFooterController @Inject constructor(
        private val stringProvider: StringProvider,
        private val userPreferencesProvider: UserPreferencesProvider
) : TypedEpoxyController<RoomListViewState>() {

    var listener: FilteredRoomFooterItem.Listener? = null

    override fun buildModels(data: RoomListViewState?) {
        val host = this
        when (data?.displayMode) {
            RoomListDisplayMode.FILTERED -> {
                filteredRoomFooterItem {
                    id("filter_footer")
                    listener(host.listener)
                    currentFilter(data.roomFilter)
                    inSpace(data.currentRoomGrouping.invoke()?.space() != null)
                }
            }
            else                         -> {
                if (userPreferencesProvider.shouldShowLongClickOnRoomHelp()) {
                    helpFooterItem {
                        id("long_click_help")
                        text(host.stringProvider.getString(R.string.help_long_click_on_room_for_more_options))
                    }
                }
            }
        }
    }
}
