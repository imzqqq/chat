package com.imzqqq.app.features.widgets.permissions

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class RoomWidgetPermissionActions : VectorViewModelAction {
    object AllowWidget : RoomWidgetPermissionActions()
    object BlockWidget : RoomWidgetPermissionActions()
    object DoClose : RoomWidgetPermissionActions()
}
