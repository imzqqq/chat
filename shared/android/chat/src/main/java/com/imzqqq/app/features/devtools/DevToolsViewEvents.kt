package com.imzqqq.app.features.devtools

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class DevToolsViewEvents : VectorViewEvents {
    object Dismiss : DevToolsViewEvents()

    //    object ShowStateList : DevToolsViewEvents()
    data class ShowAlertMessage(val message: String) : DevToolsViewEvents()
    data class ShowSnackMessage(val message: String) : DevToolsViewEvents()
}
