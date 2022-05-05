package com.imzqqq.app.features.home.room.detail.timeline.factory

import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.home.room.detail.timeline.MessageColorProvider
import com.imzqqq.app.features.home.room.detail.timeline.helper.AvatarSizeProvider
import com.imzqqq.app.features.home.room.detail.timeline.helper.MessageInformationDataFactory
import com.imzqqq.app.features.home.room.detail.timeline.helper.MessageItemAttributesFactory
import com.imzqqq.app.features.home.room.detail.timeline.item.StatusTileTimelineItem
import com.imzqqq.app.features.home.room.detail.timeline.item.StatusTileTimelineItem_
import org.matrix.android.sdk.api.extensions.orFalse
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.events.model.toModel
import org.matrix.android.sdk.internal.crypto.MXCRYPTO_ALGORITHM_MEGOLM
import org.matrix.android.sdk.internal.crypto.model.event.EncryptionEventContent
import javax.inject.Inject

class EncryptionItemFactory @Inject constructor(
        private val messageItemAttributesFactory: MessageItemAttributesFactory,
        private val messageColorProvider: MessageColorProvider,
        private val stringProvider: StringProvider,
        private val informationDataFactory: MessageInformationDataFactory,
        private val avatarSizeProvider: AvatarSizeProvider,
        private val session: Session) {

    fun create(params: TimelineItemFactoryParams): StatusTileTimelineItem? {
        val event = params.event
        if (!event.root.isStateEvent()) {
            return null
        }
        val algorithm = event.root.getClearContent().toModel<EncryptionEventContent>()?.algorithm
        val informationData = informationDataFactory.create(params)
        val attributes = messageItemAttributesFactory.create(null, informationData, params.callback)

        val isSafeAlgorithm = algorithm == MXCRYPTO_ALGORITHM_MEGOLM
        val title: String
        val description: String
        val shield: StatusTileTimelineItem.ShieldUIState
        if (isSafeAlgorithm) {
            title = stringProvider.getString(R.string.encryption_enabled)
            description = stringProvider.getString(
                    if (session.getRoomSummary(event.root.roomId ?: "")?.isDirect.orFalse()) {
                        R.string.direct_room_encryption_enabled_tile_description
                    } else {
                        R.string.encryption_enabled_tile_description
                    }
            )
            shield = StatusTileTimelineItem.ShieldUIState.BLACK
        } else {
            title = stringProvider.getString(R.string.encryption_not_enabled)
            description = stringProvider.getString(R.string.encryption_unknown_algorithm_tile_description)
            shield = StatusTileTimelineItem.ShieldUIState.RED
        }
        return StatusTileTimelineItem_()
                .attributes(
                        StatusTileTimelineItem.Attributes(
                                title = title,
                                description = description,
                                shieldUIState = shield,
                                informationData = informationData,
                                avatarRenderer = attributes.avatarRenderer,
                                messageColorProvider = messageColorProvider,
                                emojiTypeFace = attributes.emojiTypeFace,
                                itemClickListener = attributes.itemClickListener,
                                itemLongClickListener = attributes.itemLongClickListener,
                                reactionPillCallback = attributes.reactionPillCallback,
                                readReceiptsCallback = attributes.readReceiptsCallback
                        )
                )
                .highlighted(params.isHighlighted)
                .leftGuideline(avatarSizeProvider.leftGuideline)
    }
}
