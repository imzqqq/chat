package com.imzqqq.app.features.settings.account.deactivation

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class DeactivateAccountAction : VectorViewModelAction {
    data class DeactivateAccount(val eraseAllData: Boolean) : DeactivateAccountAction()

    object SsoAuthDone : DeactivateAccountAction()
    data class PasswordAuthDone(val password: String) : DeactivateAccountAction()
    object ReAuthCancelled : DeactivateAccountAction()
}
