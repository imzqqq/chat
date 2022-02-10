package com.imzqqq.app.test.fakes

import com.imzqqq.app.features.notifications.NotifiableEvent
import com.imzqqq.app.features.notifications.OutdatedEventDetector
import io.mockk.every
import io.mockk.mockk

class FakeOutdatedEventDetector {
    val instance = mockk<OutdatedEventDetector>()

    fun givenEventIsOutOfDate(notifiableEvent: NotifiableEvent) {
        every { instance.isMessageOutdated(notifiableEvent) } returns true
    }

    fun givenEventIsInDate(notifiableEvent: NotifiableEvent) {
        every { instance.isMessageOutdated(notifiableEvent) } returns false
    }
}
