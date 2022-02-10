package com.imzqqq.app.test.fakes

import android.app.Notification
import com.imzqqq.app.features.notifications.InviteNotifiableEvent
import com.imzqqq.app.features.notifications.NotificationUtils
import com.imzqqq.app.features.notifications.SimpleNotifiableEvent
import io.mockk.every
import io.mockk.mockk

class FakeNotificationUtils {

    val instance = mockk<NotificationUtils>()

    fun givenBuildRoomInvitationNotificationFor(event: InviteNotifiableEvent, myUserId: String): Notification {
        val mockNotification = mockk<Notification>()
        every { instance.buildRoomInvitationNotification(event, myUserId) } returns mockNotification
        return mockNotification
    }

    fun givenBuildSimpleInvitationNotificationFor(event: SimpleNotifiableEvent, myUserId: String): Notification {
        val mockNotification = mockk<Notification>()
        every { instance.buildSimpleEventNotification(event, myUserId) } returns mockNotification
        return mockNotification
    }
}
