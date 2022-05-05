package com.imzqqq.app.features.home.room.detail.timeline.factory

import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.resources.UserPreferencesProvider
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.home.room.detail.timeline.MessageColorProvider
import com.imzqqq.app.features.home.room.detail.timeline.helper.AvatarSizeProvider
import com.imzqqq.app.features.home.room.detail.timeline.helper.JitsiWidgetEventsGroup
import com.imzqqq.app.features.home.room.detail.timeline.helper.MessageInformationDataFactory
import com.imzqqq.app.features.home.room.detail.timeline.item.CallTileTimelineItem
import com.imzqqq.app.features.home.room.detail.timeline.item.CallTileTimelineItem_
import org.matrix.android.sdk.api.session.events.model.toModel
import org.matrix.android.sdk.api.session.widgets.model.WidgetContent
import org.matrix.android.sdk.api.session.widgets.model.WidgetType
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class WidgetItemFactory @Inject constructor(
        private val informationDataFactory: MessageInformationDataFactory,
        private val noticeItemFactory: NoticeItemFactory,
        private val avatarSizeProvider: AvatarSizeProvider,
        private val messageColorProvider: MessageColorProvider,
        private val avatarRenderer: AvatarRenderer,
        private val userPreferencesProvider: UserPreferencesProvider) {

    fun create(params: TimelineItemFactoryParams): VectorEpoxyModel<*>? {
        val event = params.event
        val widgetContent: WidgetContent = event.root.getClearContent().toModel() ?: return null
        val previousWidgetContent: WidgetContent? = event.root.resolvedPrevContent().toModel()

        return when (WidgetType.fromString(widgetContent.type ?: previousWidgetContent?.type ?: "")) {
            WidgetType.Jitsi -> createJitsiItem(params, widgetContent)
            // There is lot of other widget types we could improve here
            else             -> noticeItemFactory.create(params)
        }
    }

    private fun createJitsiItem(params: TimelineItemFactoryParams, widgetContent: WidgetContent): VectorEpoxyModel<*>? {
        val informationData = informationDataFactory.create(params)
        val userOfInterest = params.partialState.roomSummary?.toMatrixItem() ?: return null
        val isActiveTile = widgetContent.isActive()
        val jitsiWidgetEventsGroup = params.eventsGroup?.let { JitsiWidgetEventsGroup(it) } ?: return null
        val isCallStillActive = jitsiWidgetEventsGroup.isStillActive()
        val showHiddenEvents = userPreferencesProvider.shouldShowHiddenEvents()
        if (isActiveTile && !isCallStillActive) {
            return if (showHiddenEvents) {
                noticeItemFactory.create(params)
            } else {
                null
            }
        }
        val callStatus = if (isActiveTile && params.event.root.stateKey == params.partialState.jitsiState.widgetId) {
            if (params.partialState.jitsiState.hasJoined) {
                CallTileTimelineItem.CallStatus.IN_CALL
            } else {
                CallTileTimelineItem.CallStatus.INVITED
            }
        } else {
            CallTileTimelineItem.CallStatus.ENDED
        }
        val attributes = CallTileTimelineItem.Attributes(
                callId = jitsiWidgetEventsGroup.callId,
                callKind = CallTileTimelineItem.CallKind.CONFERENCE,
                callStatus = callStatus,
                informationData = informationData,
                avatarRenderer = avatarRenderer,
                messageColorProvider = messageColorProvider,
                itemClickListener = null,
                itemLongClickListener = null,
                reactionPillCallback = params.callback,
                readReceiptsCallback = params.callback,
                userOfInterest = userOfInterest,
                callback = params.callback,
                isStillActive = isCallStillActive,
                formattedDuration = ""
        )
        return CallTileTimelineItem_()
                .attributes(attributes)
                .highlighted(params.isHighlighted)
                .leftGuideline(avatarSizeProvider.leftGuideline)
    }
}
