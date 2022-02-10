package com.imzqqq.app.features.home.room.detail.timeline.factory

import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventController
import com.imzqqq.app.features.home.room.detail.timeline.item.ReadReceiptData
import com.imzqqq.app.features.home.room.detail.timeline.item.ReadReceiptsItem
import com.imzqqq.app.features.home.room.detail.timeline.item.ReadReceiptsItem_
import org.matrix.android.sdk.api.session.room.model.ReadReceipt
import javax.inject.Inject

class ReadReceiptsItemFactory @Inject constructor(private val avatarRenderer: AvatarRenderer) {

    fun create(eventId: String, readReceipts: List<ReadReceipt>, callback: TimelineEventController.Callback?): ReadReceiptsItem? {
        if (readReceipts.isEmpty()) {
            return null
        }
        val readReceiptsData = readReceipts
                .map {
                    ReadReceiptData(it.user.userId, it.user.avatarUrl, it.user.displayName, it.originServerTs)
                }
                .toList()

        return ReadReceiptsItem_()
                .id("read_receipts_$eventId")
                .eventId(eventId)
                .readReceipts(readReceiptsData)
                .avatarRenderer(avatarRenderer)
                .clickListener {
                    callback?.onReadReceiptsClicked(readReceiptsData)
                }
    }
}
