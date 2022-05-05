package com.imzqqq.app.features.settings.homeserver

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class HomeserverSettingsAction : VectorViewModelAction {
    object Refresh : HomeserverSettingsAction()
}
