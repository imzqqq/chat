package com.imzqqq.app.features.widgets.permissions

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class RoomWidgetPermissionViewEvents : VectorViewEvents {
    object Close : RoomWidgetPermissionViewEvents()
}
