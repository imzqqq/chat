@file:JvmName("ViewDataUtils")

package com.imzqqq.app.flow.util

import com.imzqqq.app.flow.entity.Notification
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.viewdata.NotificationViewData
import com.imzqqq.app.flow.viewdata.StatusViewData

@JvmName("statusToViewData")
fun Status.toViewData(
    alwaysShowSensitiveMedia: Boolean,
    alwaysOpenSpoiler: Boolean
): StatusViewData.Concrete {
    val visibleStatus = this.reblog ?: this

    return StatusViewData.Concrete(
        status = this,
        isShowingContent = alwaysShowSensitiveMedia || !visibleStatus.sensitive,
        isCollapsible = shouldTrimStatus(visibleStatus.content),
        isCollapsed = false,
        isExpanded = alwaysOpenSpoiler,
    )
}

@JvmName("notificationToViewData")
fun Notification.toViewData(
    alwaysShowSensitiveData: Boolean,
    alwaysOpenSpoiler: Boolean
): NotificationViewData.Concrete {
    return NotificationViewData.Concrete(
        this.type,
        this.id,
        this.account,
        this.status?.toViewData(alwaysShowSensitiveData, alwaysOpenSpoiler)
    )
}
