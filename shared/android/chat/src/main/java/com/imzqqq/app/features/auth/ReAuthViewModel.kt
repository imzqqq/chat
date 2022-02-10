package com.imzqqq.app.features.auth

import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.VectorViewModel
import org.matrix.android.sdk.api.auth.data.LoginFlowTypes
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.internal.crypto.crosssigning.toBase64NoPadding
import java.io.ByteArrayOutputStream

class ReAuthViewModel @AssistedInject constructor(
        @Assisted val initialState: ReAuthState,
        private val session: Session
) : VectorViewModel<ReAuthState, ReAuthActions, ReAuthEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<ReAuthViewModel, ReAuthState> {
        override fun create(initialState: ReAuthState): ReAuthViewModel
    }

    companion object : MavericksViewModelFactory<ReAuthViewModel, ReAuthState> by hiltMavericksViewModelFactory()

    override fun handle(action: ReAuthActions) = withState { state ->
        when (action) {
            ReAuthActions.StartSSOFallback -> {
                if (state.flowType == LoginFlowTypes.SSO) {
                    setState { copy(ssoFallbackPageWasShown = true) }
                    val ssoURL = session.getUiaSsoFallbackUrl(initialState.session ?: "")
                    _viewEvents.post(ReAuthEvents.OpenSsoURl(ssoURL))
                }
            }
            ReAuthActions.FallBackPageLoaded -> {
                setState { copy(ssoFallbackPageWasShown = true) }
            }
            ReAuthActions.FallBackPageClosed -> {
                // Should we do something here?
            }
            is ReAuthActions.ReAuthWithPass -> {
                val safeForIntentCypher = ByteArrayOutputStream().also {
                    it.use {
                        session.securelyStoreObject(action.password, initialState.resultKeyStoreAlias, it)
                    }
                }.toByteArray().toBase64NoPadding()
                _viewEvents.post(ReAuthEvents.PasswordFinishSuccess(safeForIntentCypher))
            }
        }
    }
}
