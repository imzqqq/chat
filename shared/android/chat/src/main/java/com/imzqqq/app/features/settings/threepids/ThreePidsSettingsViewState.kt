package com.imzqqq.app.features.settings.threepids

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.core.utils.ReadOnceTrue
import org.matrix.android.sdk.api.session.identity.ThreePid

data class ThreePidsSettingsViewState(
        val uiState: ThreePidsSettingsUiState = ThreePidsSettingsUiState.Idle,
        val isLoading: Boolean = false,
        val threePids: Async<List<ThreePid>> = Uninitialized,
        val pendingThreePids: Async<List<ThreePid>> = Uninitialized,
        val msisdnValidationRequests: Map<String, Async<Unit>> = emptyMap(),
        val editTextReinitiator: ReadOnceTrue = ReadOnceTrue(),
        val msisdnValidationReinitiator: Map<ThreePid, ReadOnceTrue> = emptyMap()
) : MavericksState
