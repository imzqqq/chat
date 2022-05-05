package com.imzqqq.app.features.userdirectory

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class UserListAction : VectorViewModelAction {
    data class SearchUsers(val value: String) : UserListAction()
    object ClearSearchUsers : UserListAction()
    data class AddPendingSelection(val pendingSelection: PendingSelection) : UserListAction()
    data class RemovePendingSelection(val pendingSelection: PendingSelection) : UserListAction()
    object ComputeMatrixToLinkForSharing : UserListAction()
    data class UpdateUserConsent(val consent: Boolean) : UserListAction()
    object Resumed : UserListAction()
}
