package com.imzqqq.app.features.home.room.detail.timeline.factory

import com.imzqqq.app.features.call.vectorCallService
import com.imzqqq.app.features.home.room.detail.timeline.helper.TimelineSettingsFactory
import com.imzqqq.app.features.home.room.detail.timeline.merged.MergedTimelines
import kotlinx.coroutines.CoroutineScope
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.events.model.EventType
import org.matrix.android.sdk.api.session.room.Room
import org.matrix.android.sdk.api.session.room.timeline.Timeline
import javax.inject.Inject

private val secondaryTimelineAllowedTypes = listOf(
        EventType.CALL_HANGUP,
        EventType.CALL_INVITE,
        EventType.CALL_REJECT,
        EventType.CALL_ANSWER
)

class TimelineFactory @Inject constructor(private val session: Session, private val timelineSettingsFactory: TimelineSettingsFactory) {

    fun createTimeline(coroutineScope: CoroutineScope, mainRoom: Room, eventId: String?): Timeline {
        val settings = timelineSettingsFactory.create()
        if (!session.vectorCallService.protocolChecker.supportVirtualRooms) {
            return mainRoom.createTimeline(eventId, settings)
        }
        val virtualRoomId = session.vectorCallService.userMapper.virtualRoomForNativeRoom(mainRoom.roomId)
        return if (virtualRoomId == null) {
            mainRoom.createTimeline(eventId, settings)
        } else {
            val virtualRoom = session.getRoom(virtualRoomId)!!
            MergedTimelines(
                    coroutineScope = coroutineScope,
                    mainTimeline = mainRoom.createTimeline(eventId, settings),
                    secondaryTimelineParams = MergedTimelines.SecondaryTimelineParams(
                            timeline = virtualRoom.createTimeline(null, settings),
                            shouldFilterTypes = true,
                            allowedTypes = secondaryTimelineAllowedTypes
                    )
            )
        }
    }
}
