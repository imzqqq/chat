package com.imzqqq.app.test.shared

import net.lachlanmckee.timberjunit.TimberTestRule

fun createTimberTestRule(): TimberTestRule {
    return TimberTestRule.builder()
            .showThread(false)
            .showTimestamp(false)
            .onlyLogWhenTestFails(false)
            .build()
}
