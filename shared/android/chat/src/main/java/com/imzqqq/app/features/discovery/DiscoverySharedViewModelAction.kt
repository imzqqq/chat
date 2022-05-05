package com.imzqqq.app.features.discovery

sealed class DiscoverySharedViewModelAction {
    data class ChangeIdentityServer(val newUrl: String) : DiscoverySharedViewModelAction()
}
