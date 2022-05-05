package com.imzqqq.app.features.home.room.list

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.session.room.model.RoomSummary

/**
 * Transient events for RoomList
 */
sealed class RoomListViewEvents : VectorViewEvents {
    data class Loading(val message: CharSequence? = null) : RoomListViewEvents()
    data class Failure(val throwable: Throwable) : RoomListViewEvents()

    data class SelectRoom(val roomSummary: RoomSummary) : RoomListViewEvents()
    object Done : RoomListViewEvents()
    data class NavigateToMxToBottomSheet(val link: String) : RoomListViewEvents()
}
