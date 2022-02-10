package com.imzqqq.app.features.discovery

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.session.identity.SharedState
import org.matrix.android.sdk.api.session.identity.ThreePid

data class PidInfo(
        // Retrieved from the homeserver
        val threePid: ThreePid,
        // Retrieved from IdentityServer, or transient state
        val isShared: Async<SharedState>,
        // Contains information about a current request to submit the token (for instance SMS code received by SMS)
        // Or a current binding finalization, for email
        val finalRequest: Async<Unit> = Uninitialized
)
