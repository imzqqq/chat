package com.imzqqq.app.features.spaces

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.RoomGroupingMethod
import org.matrix.android.sdk.api.session.group.model.GroupSummary
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.summary.RoomAggregateNotificationCount
import org.matrix.android.sdk.api.util.MatrixItem

data class SpaceListViewState(
        val myMxItem: Async<MatrixItem.UserItem> = Uninitialized,
        val asyncSpaces: Async<List<RoomSummary>> = Uninitialized,
        val selectedGroupingMethod: RoomGroupingMethod = RoomGroupingMethod.BySpace(null),
        val rootSpacesOrdered: List<RoomSummary>? = null,
        val spaceOrderInfo: Map<String, String?>? = null,
        val spaceOrderLocalEchos: Map<String, String?>? = null,
        val legacyGroups: List<GroupSummary>? = null,
        val expandedStates: Map<String, Boolean> = emptyMap(),
        val homeAggregateCount: RoomAggregateNotificationCount = RoomAggregateNotificationCount(0, 0, 0)
) : MavericksState
