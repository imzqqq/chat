package com.imzqqq.app.core.utils

import org.amshove.kluent.shouldBe
import org.junit.Test
import java.lang.Thread.sleep

class TemporaryStoreTest {

    @Test
    fun testTemporaryStore() {
        // Keep the data 30 millis
        val store = TemporaryStore<String>(30)

        store.data = "test"
        store.data shouldBe "test"
        sleep(15)
        store.data shouldBe "test"
        sleep(20)
        store.data shouldBe null
    }
}
