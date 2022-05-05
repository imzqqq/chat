package com.imzqqq.app

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.imzqqq.app.test.shared.createTimberTestRule
import org.junit.Rule

interface InstrumentedTest {

    @Rule
    fun timberTestRule() = createTimberTestRule()

    fun context(): Context {
        return ApplicationProvider.getApplicationContext()
    }
}
