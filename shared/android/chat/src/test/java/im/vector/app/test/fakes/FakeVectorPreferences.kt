package com.imzqqq.app.test.fakes

import com.imzqqq.app.features.settings.VectorPreferences
import io.mockk.every
import io.mockk.mockk

class FakeVectorPreferences {

    val instance = mockk<VectorPreferences>()

    fun givenUseCompleteNotificationFormat(value: Boolean) {
        every { instance.useCompleteNotificationFormat() } returns value
    }
}
