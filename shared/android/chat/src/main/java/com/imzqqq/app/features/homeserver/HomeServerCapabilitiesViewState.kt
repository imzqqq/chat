package com.imzqqq.app.features.homeserver

import com.airbnb.mvrx.MavericksState
import org.matrix.android.sdk.api.session.homeserver.HomeServerCapabilities

data class HomeServerCapabilitiesViewState(
        val capabilities: HomeServerCapabilities = HomeServerCapabilities(),
        val isE2EByDefault: Boolean = true
) : MavericksState
