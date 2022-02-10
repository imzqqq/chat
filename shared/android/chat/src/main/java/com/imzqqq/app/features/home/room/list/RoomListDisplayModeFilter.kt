package com.imzqqq.app.features.home.room.list

import androidx.core.util.Predicate
import com.imzqqq.app.features.home.RoomListDisplayMode
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.model.RoomSummary

class RoomListDisplayModeFilter(private val displayMode: RoomListDisplayMode) : Predicate<RoomSummary> {

    override fun test(roomSummary: RoomSummary): Boolean {
        if (roomSummary.membership.isLeft()) {
            return false
        }
        return when (displayMode) {
            RoomListDisplayMode.ALL -> true
            RoomListDisplayMode.NOTIFICATIONS ->
                roomSummary.notificationCountOrMarkedUnread() > 0 || roomSummary.membership == Membership.INVITE || roomSummary.userDrafts.isNotEmpty()
            RoomListDisplayMode.PEOPLE        -> roomSummary.isDirect && roomSummary.membership.isActive()
            RoomListDisplayMode.ROOMS         -> !roomSummary.isDirect && roomSummary.membership.isActive()
            RoomListDisplayMode.FILTERED      -> roomSummary.membership == Membership.JOIN
        }
    }
}
