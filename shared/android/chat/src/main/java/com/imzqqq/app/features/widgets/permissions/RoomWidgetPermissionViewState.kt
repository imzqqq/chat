package com.imzqqq.app.features.widgets.permissions

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.features.widgets.WidgetArgs
import org.matrix.android.sdk.api.session.widgets.model.Widget

data class RoomWidgetPermissionViewState(
        val roomId: String,
        val widgetId: String?,
        val permissionData: Async<WidgetPermissionData> = Uninitialized
) : MavericksState {

    constructor(widgetArgs: WidgetArgs) : this(
            roomId = widgetArgs.roomId,
            widgetId = widgetArgs.widgetId
    )

    data class WidgetPermissionData(
            val widget: Widget,
            val permissionsList: List<Int> = emptyList(),
            val isWebviewWidget: Boolean = true,
            val widgetDomain: String? = null
    )
}
