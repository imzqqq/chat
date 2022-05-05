package com.imzqqq.app.features.home.room.list

import org.matrix.android.sdk.api.session.room.model.RoomSummary
import javax.inject.Inject

class AlphabeticalRoomComparator @Inject constructor() : Comparator<RoomSummary> {

    override fun compare(leftRoomSummary: RoomSummary?, rightRoomSummary: RoomSummary?): Int {
        return when {
            rightRoomSummary?.displayName == null -> -1
            leftRoomSummary?.displayName == null  -> 1
            else                                  -> leftRoomSummary.displayName.compareTo(rightRoomSummary.displayName)
        }
    }
}
