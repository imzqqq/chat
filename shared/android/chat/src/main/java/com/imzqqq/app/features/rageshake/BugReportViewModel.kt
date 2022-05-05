package com.imzqqq.app.features.rageshake

import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.EmptyAction
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.extensions.tryOrNull

class BugReportViewModel @AssistedInject constructor(
        @Assisted initialState: BugReportState,
        val activeSessionHolder: ActiveSessionHolder
) : VectorViewModel<BugReportState, EmptyAction, EmptyViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<BugReportViewModel, BugReportState> {
        override fun create(initialState: BugReportState): BugReportViewModel
    }

    companion object : MavericksViewModelFactory<BugReportViewModel, BugReportState>  by hiltMavericksViewModelFactory()

    init {
        fetchHomeserverVersion()
    }

    private fun fetchHomeserverVersion() {
        viewModelScope.launch {
            val version = tryOrNull {
                activeSessionHolder.getSafeActiveSession()
                        ?.federationService()
                        ?.getFederationVersion()
                        ?.let { "${it.name} - ${it.version}" }
            } ?: "undefined"

            setState {
                copy(
                        serverVersion = version
                )
            }
        }
    }

    override fun handle(action: EmptyAction) {
        // No op
    }
}
