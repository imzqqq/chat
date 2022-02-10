package com.imzqqq.app.features.call.transfer

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class CallTransferViewEvents : VectorViewEvents {
    object Dismiss : CallTransferViewEvents()
    object Loading : CallTransferViewEvents()
    object FailToTransfer : CallTransferViewEvents()
}
