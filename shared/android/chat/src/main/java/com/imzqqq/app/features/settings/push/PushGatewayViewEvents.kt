package com.imzqqq.app.features.settings.push

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class PushGatewayViewEvents : VectorViewEvents {
    data class RemovePusherFailed(val cause: Throwable) : PushGatewayViewEvents()
}
