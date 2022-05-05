package com.imzqqq.app.test.fakes

import com.imzqqq.app.features.notifications.GroupedNotificationEvents
import com.imzqqq.app.features.notifications.NotificationFactory
import com.imzqqq.app.features.notifications.OneShotNotification
import com.imzqqq.app.features.notifications.RoomNotification
import com.imzqqq.app.features.notifications.SummaryNotification
import io.mockk.every
import io.mockk.mockk

class FakeNotificationFactory {

    val instance = mockk<NotificationFactory>()

    fun givenNotificationsFor(groupedEvents: GroupedNotificationEvents,
                              myUserId: String,
                              myUserDisplayName: String,
                              myUserAvatarUrl: String?,
                              useCompleteNotificationFormat: Boolean,
                              roomNotifications: List<RoomNotification>,
                              invitationNotifications: List<OneShotNotification>,
                              simpleNotifications: List<OneShotNotification>,
                              summaryNotification: SummaryNotification) {
        with(instance) {
            every { groupedEvents.roomEvents.toNotifications(myUserDisplayName, myUserAvatarUrl) } returns roomNotifications
            every { groupedEvents.invitationEvents.toNotifications(myUserId) } returns invitationNotifications
            every { groupedEvents.simpleEvents.toNotifications(myUserId) } returns simpleNotifications

            every {
                createSummaryNotification(
                        roomNotifications,
                        invitationNotifications,
                        simpleNotifications,
                        useCompleteNotificationFormat
                )
            } returns summaryNotification
        }
    }
}
