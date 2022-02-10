package com.imzqqq.app.features.home.room.detail.timeline.factory

import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.home.room.detail.timeline.format.NoticeEventFormatter
import com.imzqqq.app.features.home.room.detail.timeline.helper.AvatarSizeProvider
import com.imzqqq.app.features.home.room.detail.timeline.helper.MessageInformationDataFactory
import com.imzqqq.app.features.home.room.detail.timeline.item.NoticeItem
import com.imzqqq.app.features.home.room.detail.timeline.item.NoticeItem_
import org.matrix.android.sdk.api.extensions.orFalse
import javax.inject.Inject

class NoticeItemFactory @Inject constructor(private val eventFormatter: NoticeEventFormatter,
                                            private val avatarRenderer: AvatarRenderer,
                                            private val informationDataFactory: MessageInformationDataFactory,
                                            private val avatarSizeProvider: AvatarSizeProvider) {

    fun create(params: TimelineItemFactoryParams): NoticeItem? {
        val event = params.event
        val formattedText = eventFormatter.format(event, isDm = params.partialState.roomSummary?.isDirect.orFalse()) ?: return null
        val informationData = informationDataFactory.create(params)
        val attributes = NoticeItem.Attributes(
                avatarRenderer = avatarRenderer,
                informationData = informationData,
                noticeText = formattedText,
                itemLongClickListener = { view ->
                    params.callback?.onEventLongClicked(informationData, null, view) ?: false
                },
                readReceiptsCallback = params.callback,
                avatarClickListener = { params.callback?.onAvatarClicked(informationData) }
        )
        return NoticeItem_()
                .leftGuideline(avatarSizeProvider.leftGuideline)
                .highlighted(params.isHighlighted)
                .attributes(attributes)
    }
}
