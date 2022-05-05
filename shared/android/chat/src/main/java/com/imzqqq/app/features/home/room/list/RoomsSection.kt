@file:Suppress("DEPRECATION")

package com.imzqqq.app.features.home.room.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.summary.RoomAggregateNotificationCount

data class RoomsSection(
        val sectionName: String,
        // can be a paged list or a regular list
        val livePages: LiveData<PagedList<RoomSummary>>? = null,
        val liveList: LiveData<List<RoomSummary>>? = null,
        val liveSuggested: LiveData<SuggestedRoomInfo>? = null,
        val isExpanded: MutableLiveData<Boolean> = MutableLiveData(true),
        val notificationCount: MutableLiveData<RoomAggregateNotificationCount> = MutableLiveData(RoomAggregateNotificationCount(0, 0, 0)),
        val notifyOfLocalEcho: Boolean = false
)
