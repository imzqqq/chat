package com.imzqqq.app.features.share

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Incomplete
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.epoxy.noResultItem
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.home.room.list.RoomSummaryItemFactory
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import javax.inject.Inject

class IncomingShareController @Inject constructor(private val roomSummaryItemFactory: RoomSummaryItemFactory,
                                                  private val stringProvider: StringProvider) : TypedEpoxyController<IncomingShareViewState>() {

    interface Callback {
        fun onRoomClicked(roomSummary: RoomSummary)
        fun onRoomLongClicked(roomSummary: RoomSummary): Boolean
    }

    var callback: Callback? = null

    override fun buildModels(data: IncomingShareViewState) {
        val host = this
        if (data.sharedData == null || data.filteredRoomSummaries is Incomplete) {
            loadingItem {
                id("loading")
            }
            return
        }
        val roomSummaries = data.filteredRoomSummaries()
        if (roomSummaries.isNullOrEmpty()) {
            noResultItem {
                id("no_result")
                text(host.stringProvider.getString(R.string.no_result_placeholder))
            }
        } else {
            roomSummaries.forEach { roomSummary ->
                roomSummaryItemFactory
                        .createRoomItem(roomSummary, data.selectedRoomIds, callback?.let { it::onRoomClicked }, callback?.let { it::onRoomLongClicked })
                        .addTo(this)
            }
        }
    }
}
