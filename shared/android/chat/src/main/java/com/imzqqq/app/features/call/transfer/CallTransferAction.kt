package com.imzqqq.app.features.call.transfer

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class CallTransferAction : VectorViewModelAction {
    data class ConnectWithUserId(val consultFirst: Boolean, val selectedUserId: String) : CallTransferAction()
    data class ConnectWithPhoneNumber(val consultFirst: Boolean, val phoneNumber: String) : CallTransferAction()
}
