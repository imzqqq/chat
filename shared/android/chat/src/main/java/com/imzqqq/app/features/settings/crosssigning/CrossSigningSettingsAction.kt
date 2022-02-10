package com.imzqqq.app.features.settings.crosssigning

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class CrossSigningSettingsAction : VectorViewModelAction {
    object InitializeCrossSigning : CrossSigningSettingsAction()
    object SsoAuthDone : CrossSigningSettingsAction()
    data class PasswordAuthDone(val password: String) : CrossSigningSettingsAction()
    object ReAuthCancelled : CrossSigningSettingsAction()
}
