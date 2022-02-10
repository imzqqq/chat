package com.imzqqq.app.features.home

import com.imzqqq.app.R
import com.imzqqq.app.features.home.room.detail.timeline.helper.MatrixItemColorProvider.Companion.getColorFromUserId
import org.junit.Assert.assertEquals
import org.junit.Test

class UserColorTest {

    @Test
    fun testNull() {
        assertEquals(R.color.element_name_01, getColorFromUserId(null))
    }

    @Test
    fun testEmpty() {
        assertEquals(R.color.element_name_01, getColorFromUserId(""))
    }

    @Test
    fun testName() {
        assertEquals(R.color.element_name_01, getColorFromUserId("@ganfra:chat.dingshunyu.top"))
        assertEquals(R.color.element_name_04, getColorFromUserId("@benoit0816:chat.dingshunyu.top"))
        assertEquals(R.color.element_name_05, getColorFromUserId("@hubert:uhoreg.ca"))
        assertEquals(R.color.element_name_07, getColorFromUserId("@nadonomy:chat.dingshunyu.top"))
    }
}
