package com.imzqqq.app.features.home.room.detail.timeline.action

import android.os.Parcelable
import com.imzqqq.app.features.home.room.detail.timeline.item.MessageInformationData
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimelineEventFragmentArgs(
        val eventId: String,
        val roomId: String,
        val informationData: MessageInformationData
) : Parcelable
