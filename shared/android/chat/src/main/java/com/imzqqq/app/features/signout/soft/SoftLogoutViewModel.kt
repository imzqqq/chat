package com.imzqqq.app.features.signout.soft

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.hasUnsavedKeys
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.login.LoginMode
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.auth.AuthenticationService
import org.matrix.android.sdk.api.auth.data.LoginFlowTypes
import org.matrix.android.sdk.api.session.Session
import timber.log.Timber

/**
 * TODO Test push: disable the pushers?
 */
class SoftLogoutViewModel @AssistedInject constructor(
        @Assisted initialState: SoftLogoutViewState,
        private val session: Session,
        private val activeSessionHolder: ActiveSessionHolder,
        private val authenticationService: AuthenticationService
) : VectorViewModel<SoftLogoutViewState, SoftLogoutAction, SoftLogoutViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<SoftLogoutViewModel, SoftLogoutViewState> {
        override fun create(initialState: SoftLogoutViewState): SoftLogoutViewModel
    }

    companion object : MavericksViewModelFactory<SoftLogoutViewModel, SoftLogoutViewState> by hiltMavericksViewModelFactory() {

        override fun initialState(viewModelContext: ViewModelContext): SoftLogoutViewState? {
            val activity: SoftLogoutActivity = (viewModelContext as ActivityViewModelContext).activity()
            val userId = activity.session.myUserId
            return SoftLogoutViewState(
                    homeServerUrl = activity.session.sessionParams.homeServerUrl,
                    userId = userId,
                    deviceId = activity.session.sessionParams.deviceId ?: "",
                    userDisplayName = activity.session.getUser(userId)?.displayName ?: userId,
                    hasUnsavedKeys = activity.session.hasUnsavedKeys()
            )
        }
    }

    init {
        // Get the supported login flow
        getSupportedLoginFlow()
    }

    private fun getSupportedLoginFlow() {
        viewModelScope.launch {
            authenticationService.cancelPendingLoginOrRegistration()

            setState {
                copy(
                        asyncHomeServerLoginFlowRequest = Loading()
                )
            }

            val data = try {
                authenticationService.getLoginFlowOfSession(session.sessionId)
            } catch (failure: Throwable) {
                setState {
                    copy(
                            asyncHomeServerLoginFlowRequest = Fail(failure)
                    )
                }
                null
            }

            data ?: return@launch

            val loginMode = when {
                // SSO login is taken first
                data.supportedLoginTypes.contains(LoginFlowTypes.SSO) &&
                        data.supportedLoginTypes.contains(LoginFlowTypes.PASSWORD) -> LoginMode.SsoAndPassword(data.ssoIdentityProviders)
                data.supportedLoginTypes.contains(LoginFlowTypes.SSO)              -> LoginMode.Sso(data.ssoIdentityProviders)
                data.supportedLoginTypes.contains(LoginFlowTypes.PASSWORD)         -> LoginMode.Password
                else                                                               -> LoginMode.Unsupported
            }

            setState {
                copy(
                        asyncHomeServerLoginFlowRequest = Success(loginMode)
                )
            }
        }
    }

    override fun handle(action: SoftLogoutAction) {
        when (action) {
            is SoftLogoutAction.RetryLoginFlow  -> getSupportedLoginFlow()
            is SoftLogoutAction.PasswordChanged -> handlePasswordChange(action)
            is SoftLogoutAction.SignInAgain     -> handleSignInAgain(action)
            is SoftLogoutAction.WebLoginSuccess -> handleWebLoginSuccess(action)
            is SoftLogoutAction.ClearData       -> handleClearData()
        }
    }

    private fun handleClearData() {
        // Notify the Activity
        _viewEvents.post(SoftLogoutViewEvents.ClearData)
    }

    private fun handlePasswordChange(action: SoftLogoutAction.PasswordChanged) {
        setState {
            copy(
                    asyncLoginAction = Uninitialized,
                    enteredPassword = action.password
            )
        }
    }

    private fun handleWebLoginSuccess(action: SoftLogoutAction.WebLoginSuccess) {
        // User may have been connected with SSO with another userId
        // We have to check this
        withState { softLogoutViewState ->
            if (softLogoutViewState.userId != action.credentials.userId) {
                Timber.w("User login again with SSO, but using another account")
                _viewEvents.post(SoftLogoutViewEvents.ErrorNotSameUser(
                        softLogoutViewState.userId,
                        action.credentials.userId))
            } else {
                setState {
                    copy(
                            asyncLoginAction = Loading()
                    )
                }
                viewModelScope.launch {
                    try {
                        session.updateCredentials(action.credentials)
                        onSessionRestored()
                    } catch (failure: Throwable) {
                        _viewEvents.post(SoftLogoutViewEvents.Failure(failure))
                        setState {
                            copy(
                                    asyncLoginAction = Uninitialized
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleSignInAgain(action: SoftLogoutAction.SignInAgain) {
        setState {
            copy(
                    asyncLoginAction = Loading()
            )
        }
        viewModelScope.launch {
            try {
                session.signInAgain(action.password)
                onSessionRestored()
            } catch (failure: Throwable) {
                setState {
                    copy(
                            asyncLoginAction = Fail(failure)
                    )
                }
            }
        }
    }

    private fun onSessionRestored() {
        activeSessionHolder.setActiveSession(session)
        // Start the sync
        session.startSync(true)

        // TODO Configure and start ? Check that the push still works...
        setState {
            copy(
                    asyncLoginAction = Success(Unit)
            )
        }
    }
}
