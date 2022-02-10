package com.imzqqq.app.features.home.room.list

import androidx.core.util.Predicate
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import javax.inject.Inject

class RoomListNameFilter @Inject constructor() : Predicate<RoomSummary> {

    var filter: String = ""

    override fun test(roomSummary: RoomSummary): Boolean {
        if (filter.isEmpty()) {
            // No filter
            return true
        }

        return roomSummary.displayName.contains(filter, ignoreCase = true)
    }
}
