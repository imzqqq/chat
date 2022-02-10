package com.imzqqq.app.features.discovery.change

import com.airbnb.mvrx.MavericksState

data class SetIdentityServerState(
        val homeServerUrl: String = "",
        // Will contain the default identity server url if any
        val defaultIdentityServerUrl: String? = null
) : MavericksState
