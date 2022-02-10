package com.imzqqq.app.features.settings.account.deactivation

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.auth.ReAuthActivity
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.auth.UIABaseAuth
import org.matrix.android.sdk.api.auth.UserInteractiveAuthInterceptor
import org.matrix.android.sdk.api.auth.UserPasswordAuth
import org.matrix.android.sdk.api.auth.registration.RegistrationFlowResponse
import org.matrix.android.sdk.api.failure.isInvalidUIAAuth
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.internal.crypto.crosssigning.fromBase64
import org.matrix.android.sdk.internal.crypto.model.rest.DefaultBaseAuth
import timber.log.Timber
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class DeactivateAccountViewState(
        val dummy: Boolean = false
) : MavericksState

class DeactivateAccountViewModel @AssistedInject constructor(@Assisted private val initialState: DeactivateAccountViewState,
                                                             private val session: Session) :
    VectorViewModel<DeactivateAccountViewState, DeactivateAccountAction, DeactivateAccountViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<DeactivateAccountViewModel, DeactivateAccountViewState> {
        override fun create(initialState: DeactivateAccountViewState): DeactivateAccountViewModel
    }

    var uiaContinuation: Continuation<UIABaseAuth>? = null
    var pendingAuth: UIABaseAuth? = null

    override fun handle(action: DeactivateAccountAction) {
        when (action) {
            is DeactivateAccountAction.DeactivateAccount -> handleDeactivateAccount(action)
            DeactivateAccountAction.SsoAuthDone -> {
                Timber.d("## UIA - FallBack success")
                if (pendingAuth != null) {
                    uiaContinuation?.resume(pendingAuth!!)
                } else {
                    uiaContinuation?.resumeWithException(IllegalArgumentException())
                }
            }
            is DeactivateAccountAction.PasswordAuthDone -> {
                val decryptedPass = session.loadSecureSecret<String>(action.password.fromBase64().inputStream(), ReAuthActivity.DEFAULT_RESULT_KEYSTORE_ALIAS)
                uiaContinuation?.resume(
                        UserPasswordAuth(
                                session = pendingAuth?.session,
                                password = decryptedPass,
                                user = session.myUserId
                        )
                )
            }
            DeactivateAccountAction.ReAuthCancelled -> {
                Timber.d("## UIA - Reauth cancelled")
                uiaContinuation?.resumeWithException(Exception())
                uiaContinuation = null
                pendingAuth = null
            }
        }.exhaustive
    }

    private fun handleDeactivateAccount(action: DeactivateAccountAction.DeactivateAccount) {
        _viewEvents.post(DeactivateAccountViewEvents.Loading())

        viewModelScope.launch {
            val event = try {
                session.deactivateAccount(
                        action.eraseAllData,
                        object : UserInteractiveAuthInterceptor {
                            override fun performStage(flowResponse: RegistrationFlowResponse, errCode: String?, promise: Continuation<UIABaseAuth>) {
                                _viewEvents.post(DeactivateAccountViewEvents.RequestReAuth(flowResponse, errCode))
                                pendingAuth = DefaultBaseAuth(session = flowResponse.session)
                                uiaContinuation = promise
                            }
                        }
                )
                DeactivateAccountViewEvents.Done
            } catch (failure: Exception) {
                if (failure.isInvalidUIAAuth()) {
                    DeactivateAccountViewEvents.InvalidAuth
                } else {
                    DeactivateAccountViewEvents.OtherFailure(failure)
                }
            }

            _viewEvents.post(event)
        }
    }

    companion object : MavericksViewModelFactory<DeactivateAccountViewModel, DeactivateAccountViewState> by hiltMavericksViewModelFactory()
}
