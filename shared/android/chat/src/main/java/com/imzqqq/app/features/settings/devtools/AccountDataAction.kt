package com.imzqqq.app.features.settings.devtools

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class AccountDataAction : VectorViewModelAction {
    data class DeleteAccountData(val type: String) : AccountDataAction()
}
