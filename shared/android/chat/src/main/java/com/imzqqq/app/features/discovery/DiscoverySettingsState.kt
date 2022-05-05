package com.imzqqq.app.features.discovery

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized

data class DiscoverySettingsState(
        val identityServer: Async<IdentityServerWithTerms?> = Uninitialized,
        val emailList: Async<List<PidInfo>> = Uninitialized,
        val phoneNumbersList: Async<List<PidInfo>> = Uninitialized,
        // Can be true if terms are updated
        val termsNotSigned: Boolean = false,
        val userConsent: Boolean = false,
        val isIdentityPolicyUrlsExpanded: Boolean = false
) : MavericksState

data class IdentityServerWithTerms(
        val serverUrl: String,
        val policies: List<IdentityServerPolicy>
)

data class IdentityServerPolicy(val name: String, val url: String)
