package com.imzqqq.app.features.settings.homeserver

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.federation.FederationVersion
import org.matrix.android.sdk.api.session.homeserver.HomeServerCapabilities

data class HomeServerSettingsViewState(
        val homeserverUrl: String = "",
        val homeserverClientServerApiUrl: String = "",
        val homeServerCapabilities: HomeServerCapabilities = HomeServerCapabilities(),
        val federationVersion: Async<FederationVersion> = Uninitialized
) : MavericksState
