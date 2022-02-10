package com.imzqqq.app.features.homeserver

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoints
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.SingletonEntryPoint
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.EmptyAction
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.raw.wellknown.getElementWellknown
import com.imzqqq.app.features.raw.wellknown.isE2EByDefault
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.extensions.tryOrNull
import org.matrix.android.sdk.api.raw.RawService
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.homeserver.HomeServerCapabilities

class HomeServerCapabilitiesViewModel @AssistedInject constructor(
        @Assisted initialState: HomeServerCapabilitiesViewState,
        private val session: Session,
        private val rawService: RawService
) : VectorViewModel<HomeServerCapabilitiesViewState, EmptyAction, EmptyViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<HomeServerCapabilitiesViewModel, HomeServerCapabilitiesViewState> {
        override fun create(initialState: HomeServerCapabilitiesViewState): HomeServerCapabilitiesViewModel
    }

    companion object : MavericksViewModelFactory<HomeServerCapabilitiesViewModel, HomeServerCapabilitiesViewState> by hiltMavericksViewModelFactory() {

        override fun initialState(viewModelContext: ViewModelContext): HomeServerCapabilitiesViewState {
            val session = EntryPoints.get(viewModelContext.app(), SingletonEntryPoint::class.java).activeSessionHolder().getSafeActiveSession()
            return HomeServerCapabilitiesViewState(
                    capabilities = session?.getHomeServerCapabilities() ?: HomeServerCapabilities()
            )
        }
    }

    init {

        initAdminE2eByDefault()
    }

    private fun initAdminE2eByDefault() {
        viewModelScope.launch(Dispatchers.IO) {
            val adminE2EByDefault = tryOrNull {
                rawService.getElementWellknown(session.sessionParams)
                        ?.isE2EByDefault()
                        ?: true
            } ?: true

            setState {
                copy(
                        isE2EByDefault = adminE2EByDefault
                )
            }
        }
    }

    override fun handle(action: EmptyAction) {}
}
