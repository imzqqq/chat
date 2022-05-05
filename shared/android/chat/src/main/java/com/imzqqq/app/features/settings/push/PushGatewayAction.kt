package com.imzqqq.app.features.settings.push

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.session.pushers.Pusher

sealed class PushGatewayAction : VectorViewModelAction {
    object Refresh : PushGatewayAction()
    data class RemovePusher(val pusher: Pusher) : PushGatewayAction()
}
