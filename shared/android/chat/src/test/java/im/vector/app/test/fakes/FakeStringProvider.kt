package com.imzqqq.app.test.fakes

import com.imzqqq.app.core.resources.StringProvider
import io.mockk.every
import io.mockk.mockk

class FakeStringProvider {

    val instance = mockk<StringProvider>()

    init {
        every { instance.getString(any()) } answers {
            "test-${args[0]}"
        }
    }
}
