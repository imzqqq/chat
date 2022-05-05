package com.imzqqq.app.features.discovery

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class DiscoverySettingsViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : DiscoverySettingsViewEvents()
}
