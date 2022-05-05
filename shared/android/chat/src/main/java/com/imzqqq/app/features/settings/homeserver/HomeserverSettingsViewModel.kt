package com.imzqqq.app.features.settings.homeserver

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session

class HomeserverSettingsViewModel @AssistedInject constructor(
        @Assisted initialState: HomeServerSettingsViewState,
        private val session: Session
) : VectorViewModel<HomeServerSettingsViewState, HomeserverSettingsAction, EmptyViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<HomeserverSettingsViewModel, HomeServerSettingsViewState> {
        override fun create(initialState: HomeServerSettingsViewState): HomeserverSettingsViewModel
    }

    companion object : MavericksViewModelFactory<HomeserverSettingsViewModel, HomeServerSettingsViewState> by hiltMavericksViewModelFactory()

    init {
        setState {
            copy(
                    homeserverUrl = session.sessionParams.homeServerUrl,
                    homeserverClientServerApiUrl = session.sessionParams.homeServerUrlBase,
                    homeServerCapabilities = session.getHomeServerCapabilities()
            )
        }
        fetchHomeserverVersion()
        refreshHomeServerCapabilities()
    }

    private fun refreshHomeServerCapabilities() {
        viewModelScope.launch {
            runCatching {
                session.refreshHomeServerCapabilities()
            }

            setState {
                copy(
                        homeServerCapabilities = session.getHomeServerCapabilities()
                )
            }
        }
    }

    private fun fetchHomeserverVersion() {
        setState {
            copy(
                    federationVersion = Loading()
            )
        }

        viewModelScope.launch {
            try {
                val federationVersion = session.federationService().getFederationVersion()
                setState {
                    copy(
                            federationVersion = Success(federationVersion)
                    )
                }
            } catch (failure: Throwable) {
                setState {
                    copy(
                            federationVersion = Fail(failure)
                    )
                }
            }
        }
    }

    override fun handle(action: HomeserverSettingsAction) {
        when (action) {
            HomeserverSettingsAction.Refresh -> fetchHomeserverVersion()
        }
    }
}
