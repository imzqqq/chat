package com.imzqqq.app.features.home.room.detail.timeline.factory

import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.resources.UserPreferencesProvider
import com.imzqqq.app.features.home.room.detail.timeline.MessageColorProvider
import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventController
import com.imzqqq.app.features.home.room.detail.timeline.helper.AvatarSizeProvider
import com.imzqqq.app.features.home.room.detail.timeline.helper.CallSignalingEventsGroup
import com.imzqqq.app.features.home.room.detail.timeline.helper.MessageInformationDataFactory
import com.imzqqq.app.features.home.room.detail.timeline.helper.MessageItemAttributesFactory
import com.imzqqq.app.features.home.room.detail.timeline.item.CallTileTimelineItem
import com.imzqqq.app.features.home.room.detail.timeline.item.CallTileTimelineItem_
import com.imzqqq.app.features.home.room.detail.timeline.item.MessageInformationData
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.events.model.EventType
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class CallItemFactory @Inject constructor(
        private val session: Session,
        private val userPreferencesProvider: UserPreferencesProvider,
        private val messageColorProvider: MessageColorProvider,
        private val messageInformationDataFactory: MessageInformationDataFactory,
        private val messageItemAttributesFactory: MessageItemAttributesFactory,
        private val avatarSizeProvider: AvatarSizeProvider,
        private val noticeItemFactory: NoticeItemFactory) {

    fun create(params: TimelineItemFactoryParams): VectorEpoxyModel<*>? {
        val event = params.event
        if (event.root.eventId == null) return null
        val showHiddenEvents = userPreferencesProvider.shouldShowHiddenEvents()
        val callEventGrouper = params.eventsGroup?.let { CallSignalingEventsGroup(it) } ?: return null
        val roomSummary = params.partialState.roomSummary ?: return null
        val informationData = messageInformationDataFactory.create(params)
        val callKind = if (callEventGrouper.isVideo()) CallTileTimelineItem.CallKind.VIDEO else CallTileTimelineItem.CallKind.AUDIO
        val callItem = when (event.root.getClearType()) {
            EventType.CALL_ANSWER -> {
                if (callEventGrouper.isInCall()) {
                    createCallTileTimelineItem(
                            roomSummary = roomSummary,
                            callId = callEventGrouper.callId,
                            callStatus = CallTileTimelineItem.CallStatus.IN_CALL,
                            callKind = callKind,
                            callback = params.callback,
                            highlight = params.isHighlighted,
                            informationData = informationData,
                            isStillActive = callEventGrouper.isInCall(),
                            formattedDuration = callEventGrouper.formattedDuration()
                    )
                } else {
                    null
                }
            }
            EventType.CALL_INVITE -> {
                if (callEventGrouper.isRinging()) {
                    createCallTileTimelineItem(
                            roomSummary = roomSummary,
                            callId = callEventGrouper.callId,
                            callStatus = CallTileTimelineItem.CallStatus.INVITED,
                            callKind = callKind,
                            callback = params.callback,
                            highlight = params.isHighlighted,
                            informationData = informationData,
                            isStillActive = callEventGrouper.isRinging(),
                            formattedDuration = callEventGrouper.formattedDuration()
                    )
                } else {
                    null
                }
            }
            EventType.CALL_REJECT -> {
                createCallTileTimelineItem(
                        roomSummary = roomSummary,
                        callId = callEventGrouper.callId,
                        callStatus = CallTileTimelineItem.CallStatus.REJECTED,
                        callKind = callKind,
                        callback = params.callback,
                        highlight = params.isHighlighted,
                        informationData = informationData,
                        isStillActive = false,
                        formattedDuration = callEventGrouper.formattedDuration()
                )
            }
            EventType.CALL_HANGUP -> {
                createCallTileTimelineItem(
                        roomSummary = roomSummary,
                        callId = callEventGrouper.callId,
                        callStatus = if (callEventGrouper.callWasMissed()) CallTileTimelineItem.CallStatus.MISSED else CallTileTimelineItem.CallStatus.ENDED,
                        callKind = callKind,
                        callback = params.callback,
                        highlight = params.isHighlighted,
                        informationData = informationData,
                        isStillActive = false,
                        formattedDuration = callEventGrouper.formattedDuration()
                )
            }
            else                  -> null
        }
        return if (callItem == null && showHiddenEvents) {
            // Fallback to notice item for showing hidden events
            noticeItemFactory.create(params)
        } else {
            callItem
        }
    }

    private fun createCallTileTimelineItem(
            roomSummary: RoomSummary,
            callId: String,
            callKind: CallTileTimelineItem.CallKind,
            callStatus: CallTileTimelineItem.CallStatus,
            informationData: MessageInformationData,
            highlight: Boolean,
            isStillActive: Boolean,
            formattedDuration: String,
            callback: TimelineEventController.Callback?
    ): CallTileTimelineItem? {
        val userOfInterest = roomSummary.toMatrixItem()
        val attributes = messageItemAttributesFactory.create(null, informationData, callback).let {
            CallTileTimelineItem.Attributes(
                    callId = callId,
                    callKind = callKind,
                    callStatus = callStatus,
                    informationData = informationData,
                    avatarRenderer = it.avatarRenderer,
                    formattedDuration = formattedDuration,
                    messageColorProvider = messageColorProvider,
                    itemClickListener = it.itemClickListener,
                    itemLongClickListener = it.itemLongClickListener,
                    reactionPillCallback = it.reactionPillCallback,
                    readReceiptsCallback = it.readReceiptsCallback,
                    userOfInterest = userOfInterest,
                    callback = callback,
                    isStillActive = isStillActive
            )
        }
        return CallTileTimelineItem_()
                .attributes(attributes)
                .highlighted(highlight)
                .leftGuideline(avatarSizeProvider.leftGuideline)
    }
}
