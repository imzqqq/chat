package com.imzqqq.app.features.discovery.change

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class SetIdentityServerAction : VectorViewModelAction {
    object UseDefaultIdentityServer : SetIdentityServerAction()

    data class UseCustomIdentityServer(val url: String) : SetIdentityServerAction()
}
