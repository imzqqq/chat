package com.imzqqq.app.features.settings.crosssigning

import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.R
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.auth.ReAuthActivity
import com.imzqqq.app.features.login.ReAuthHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.auth.UIABaseAuth
import org.matrix.android.sdk.api.auth.UserInteractiveAuthInterceptor
import org.matrix.android.sdk.api.auth.UserPasswordAuth
import org.matrix.android.sdk.api.auth.data.LoginFlowTypes
import org.matrix.android.sdk.api.auth.registration.RegistrationFlowResponse
import org.matrix.android.sdk.api.auth.registration.nextUncompletedStage
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.flow.flow
import org.matrix.android.sdk.internal.crypto.crosssigning.fromBase64
import org.matrix.android.sdk.internal.crypto.crosssigning.isVerified
import org.matrix.android.sdk.internal.crypto.model.rest.DefaultBaseAuth
import org.matrix.android.sdk.internal.util.awaitCallback
import timber.log.Timber
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CrossSigningSettingsViewModel @AssistedInject constructor(
        @Assisted private val initialState: CrossSigningSettingsViewState,
        private val session: Session,
        private val reAuthHelper: ReAuthHelper,
        private val stringProvider: StringProvider
) : VectorViewModel<CrossSigningSettingsViewState, CrossSigningSettingsAction, CrossSigningSettingsViewEvents>(initialState) {

    init {
        combine(
                session.flow().liveMyDevicesInfo(),
                session.flow().liveCrossSigningInfo(session.myUserId)
        ) { myDevicesInfo, mxCrossSigningInfo ->
            myDevicesInfo to mxCrossSigningInfo
        }
                .execute { data ->
                    val crossSigningKeys = data.invoke()?.second?.getOrNull()
                    val xSigningIsEnableInAccount = crossSigningKeys != null
                    val xSigningKeysAreTrusted = session.cryptoService().crossSigningService().checkUserTrust(session.myUserId).isVerified()
                    val xSigningKeyCanSign = session.cryptoService().crossSigningService().canCrossSign()

                    copy(
                            crossSigningInfo = crossSigningKeys,
                            xSigningIsEnableInAccount = xSigningIsEnableInAccount,
                            xSigningKeysAreTrusted = xSigningKeysAreTrusted,
                            xSigningKeyCanSign = xSigningKeyCanSign
                    )
                }
    }

    var uiaContinuation: Continuation<UIABaseAuth>? = null
    var pendingAuth: UIABaseAuth? = null

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<CrossSigningSettingsViewModel, CrossSigningSettingsViewState> {
        override fun create(initialState: CrossSigningSettingsViewState): CrossSigningSettingsViewModel
    }

    override fun handle(action: CrossSigningSettingsAction) {
        when (action) {
            CrossSigningSettingsAction.InitializeCrossSigning -> {
                _viewEvents.post(CrossSigningSettingsViewEvents.ShowModalWaitingView(null))
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        awaitCallback<Unit> {
                            session.cryptoService().crossSigningService().initializeCrossSigning(
                                    object : UserInteractiveAuthInterceptor {
                                        override fun performStage(flowResponse: RegistrationFlowResponse,
                                                                  errCode: String?,
                                                                  promise: Continuation<UIABaseAuth>) {
                                            Timber.d("## UIA : initializeCrossSigning UIA")
                                            if (flowResponse.nextUncompletedStage() == LoginFlowTypes.PASSWORD &&
                                                    reAuthHelper.data != null && errCode == null) {
                                                UserPasswordAuth(
                                                        session = null,
                                                        user = session.myUserId,
                                                        password = reAuthHelper.data
                                                ).let { promise.resume(it) }
                                            } else {
                                                Timber.d("## UIA : initializeCrossSigning UIA > start reauth activity")
                                                _viewEvents.post(CrossSigningSettingsViewEvents.RequestReAuth(flowResponse, errCode))
                                                pendingAuth = DefaultBaseAuth(session = flowResponse.session)
                                                uiaContinuation = promise
                                            }
                                        }
                                    }, it)
                        }
                    } catch (failure: Throwable) {
                        handleInitializeXSigningError(failure)
                    } finally {
                        _viewEvents.post(CrossSigningSettingsViewEvents.HideModalWaitingView)
                    }
                }
                Unit
            }
            is CrossSigningSettingsAction.SsoAuthDone         -> {
                Timber.d("## UIA - FallBack success")
                if (pendingAuth != null) {
                    uiaContinuation?.resume(pendingAuth!!)
                } else {
                    uiaContinuation?.resumeWithException(IllegalArgumentException())
                }
            }
            is CrossSigningSettingsAction.PasswordAuthDone    -> {
                val decryptedPass = session.loadSecureSecret<String>(action.password.fromBase64().inputStream(), ReAuthActivity.DEFAULT_RESULT_KEYSTORE_ALIAS)
                uiaContinuation?.resume(
                        UserPasswordAuth(
                                session = pendingAuth?.session,
                                password = decryptedPass,
                                user = session.myUserId
                        )
                )
            }
            CrossSigningSettingsAction.ReAuthCancelled        -> {
                Timber.d("## UIA - Reauth cancelled")
                _viewEvents.post(CrossSigningSettingsViewEvents.HideModalWaitingView)
                uiaContinuation?.resumeWithException(Exception())
                uiaContinuation = null
                pendingAuth = null
            }
        }.exhaustive
    }

    private fun handleInitializeXSigningError(failure: Throwable) {
        Timber.e(failure, "## CrossSigning - Failed to initialize cross signing")
        _viewEvents.post(CrossSigningSettingsViewEvents.Failure(Exception(stringProvider.getString(R.string.failed_to_initialize_cross_signing))))
    }

    companion object : MavericksViewModelFactory<CrossSigningSettingsViewModel, CrossSigningSettingsViewState> by hiltMavericksViewModelFactory()
}
