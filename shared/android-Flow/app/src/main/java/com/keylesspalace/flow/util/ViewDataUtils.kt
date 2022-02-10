@file:JvmName("ViewDataUtils")

/* Copyright 2017 Andrew Dawson
 *
 * This file is a part of Flow.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Flow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Flow; if not,
 * see <http://www.gnu.org/licenses>. */
package com.keylesspalace.flow.util

import com.keylesspalace.flow.entity.Notification
import com.keylesspalace.flow.entity.Status
import com.keylesspalace.flow.viewdata.NotificationViewData
import com.keylesspalace.flow.viewdata.StatusViewData

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
