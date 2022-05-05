package com.imzqqq.app.test.fakes

import com.imzqqq.app.features.notifications.NotifiableMessageEvent
import com.imzqqq.app.features.notifications.RoomGroupMessageCreator
import com.imzqqq.app.features.notifications.RoomNotification
import io.mockk.every
import io.mockk.mockk

class FakeRoomGroupMessageCreator {

    val instance = mockk<RoomGroupMessageCreator>()

    fun givenCreatesRoomMessageFor(events: List<NotifiableMessageEvent>,
                                   roomId: String,
                                   userDisplayName: String,
                                   userAvatarUrl: String?): RoomNotification.Message {
        val mockMessage = mockk<RoomNotification.Message>()
        every { instance.createRoomMessage(events, roomId, userDisplayName, userAvatarUrl) } returns mockMessage
        return mockMessage
    }
}
