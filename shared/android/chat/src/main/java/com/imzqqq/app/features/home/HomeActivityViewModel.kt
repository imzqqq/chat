package com.imzqqq.app.features.home

import androidx.lifecycle.asFlow
import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.features.login.ReAuthHelper
import com.imzqqq.app.features.session.coroutineScope
import com.imzqqq.app.features.settings.VectorPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.auth.UIABaseAuth
import org.matrix.android.sdk.api.auth.UserInteractiveAuthInterceptor
import org.matrix.android.sdk.api.auth.UserPasswordAuth
import org.matrix.android.sdk.api.auth.data.LoginFlowTypes
import org.matrix.android.sdk.api.auth.registration.RegistrationFlowResponse
import org.matrix.android.sdk.api.auth.registration.nextUncompletedStage
import org.matrix.android.sdk.api.extensions.tryOrNull
import org.matrix.android.sdk.api.pushrules.RuleIds
import org.matrix.android.sdk.api.session.initsync.SyncStatusService
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import org.matrix.android.sdk.api.util.toMatrixItem
import org.matrix.android.sdk.flow.flow
import org.matrix.android.sdk.internal.crypto.model.CryptoDeviceInfo
import org.matrix.android.sdk.internal.crypto.model.MXUsersDevicesMap
import org.matrix.android.sdk.internal.util.awaitCallback
import timber.log.Timber
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class HomeActivityViewModel @AssistedInject constructor(
        @Assisted initialState: HomeActivityViewState,
        private val activeSessionHolder: ActiveSessionHolder,
        private val reAuthHelper: ReAuthHelper,
        private val vectorPreferences: VectorPreferences
) : VectorViewModel<HomeActivityViewState, HomeActivityViewActions, HomeActivityViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<HomeActivityViewModel, HomeActivityViewState> {
        override fun create(initialState: HomeActivityViewState): HomeActivityViewModel
    }

    companion object : MavericksViewModelFactory<HomeActivityViewModel, HomeActivityViewState> by hiltMavericksViewModelFactory()

    private var checkBootstrap = false
    private var onceTrusted = false

    init {
        cleanupFiles()
        observeInitialSync()
        checkSessionPushIsOn()
        observeCrossSigningReset()
    }

    private fun cleanupFiles() {
        // Mitigation: delete all cached decrypted files each time the application is started.
        activeSessionHolder.getSafeActiveSession()?.fileService()?.clearDecryptedCache()
    }

    private fun observeCrossSigningReset() {
        val safeActiveSession = activeSessionHolder.getSafeActiveSession() ?: return

        onceTrusted = safeActiveSession
                .cryptoService()
                .crossSigningService().allPrivateKeysKnown()

        safeActiveSession
                .flow()
                .liveCrossSigningInfo(safeActiveSession.myUserId)
                .onEach {
                    val isVerified = it.getOrNull()?.isTrusted() ?: false
                    if (!isVerified && onceTrusted) {
                        // cross signing keys have been reset
                        // Trigger a popup to re-verify
                        // Note: user can be null in case of logout
                        safeActiveSession.getUser(safeActiveSession.myUserId)
                                ?.toMatrixItem()
                                ?.let { user ->
                                    _viewEvents.post(HomeActivityViewEvents.OnCrossSignedInvalidated(user))
                                }
                    }
                    onceTrusted = isVerified
                }
                .launchIn(viewModelScope)
    }

    private fun observeInitialSync() {
        val session = activeSessionHolder.getSafeActiveSession() ?: return

        session.getSyncStatusLive()
                .asFlow()
                .onEach { status ->
                    when (status) {
                        is SyncStatusService.Status.Progressing -> {
                            // Schedule a check of the bootstrap when the init sync will be finished
                            checkBootstrap = true
                        }
                        is SyncStatusService.Status.Idle        -> {
                            if (checkBootstrap) {
                                checkBootstrap = false
                                maybeBootstrapCrossSigningAfterInitialSync()
                            }
                        }
                        else                                    -> Unit
                    }

                    setState {
                        copy(
                                syncStatusServiceStatus = status
                        )
                    }
                }
                .launchIn(viewModelScope)
    }

    /**
     * After migration from riot to element some users reported that their
     * push setting for the session was set to off
     * In order to mitigate this, we want to display a popup once to the user
     * giving him the option to review this setting
     */
    private fun checkSessionPushIsOn() {
        viewModelScope.launch(Dispatchers.IO) {
            // Don't do that if it's a login or a register (pass in memory)
            if (reAuthHelper.data != null) return@launch
            // Check if disabled for this device
            if (!vectorPreferences.areNotificationEnabledForDevice()) {
                // Check if set at account level
                val mRuleMaster = activeSessionHolder.getSafeActiveSession()
                        ?.getPushRules()
                        ?.getAllRules()
                        ?.find { it.ruleId == RuleIds.RULE_ID_DISABLE_ALL }
                if (mRuleMaster?.enabled == false) {
                    // So push are enabled at account level but not for this session
                    // Let's check that there are some rooms?
                    val knownRooms = activeSessionHolder.getSafeActiveSession()?.getRoomSummaries(roomSummaryQueryParams {
                        memberships = Membership.activeMemberships()
                    })?.size ?: 0

                    // Prompt once to the user
                    if (knownRooms > 1 && !vectorPreferences.didAskUserToEnableSessionPush()) {
                        // delay a bit
                        delay(1500)
                        _viewEvents.post(HomeActivityViewEvents.PromptToEnableSessionPush)
                    }
                }
            }
        }
    }

    private fun maybeBootstrapCrossSigningAfterInitialSync() {
        // We do not use the viewModel context because we do not want to tie this action to activity view model
        activeSessionHolder.getSafeActiveSession()?.coroutineScope?.launch(Dispatchers.IO) {
            val session = activeSessionHolder.getSafeActiveSession() ?: return@launch

            tryOrNull("## MaybeBootstrapCrossSigning: Failed to download keys") {
                awaitCallback<MXUsersDevicesMap<CryptoDeviceInfo>> {
                    session.cryptoService().downloadKeys(listOf(session.myUserId), true, it)
                }
            }

            // From there we are up to date with server
            // Is there already cross signing keys here?
            val mxCrossSigningInfo = session.cryptoService().crossSigningService().getMyCrossSigningKeys()
            if (mxCrossSigningInfo != null) {
                // Cross-signing is already set up for this user, is it trusted?
                if (!mxCrossSigningInfo.isTrusted()) {
                    // New session
                    _viewEvents.post(
                            HomeActivityViewEvents.OnNewSession(
                                    session.getUser(session.myUserId)?.toMatrixItem(),
                                    // Always send request instead of waiting for an incoming as per recent EW changes
                                    false
                            )
                    )
                }
            } else {
                // Try to initialize cross signing in background if possible
                Timber.d("Initialize cross signing...")
                try {
                    awaitCallback<Unit> {
                        session.cryptoService().crossSigningService().initializeCrossSigning(
                                object : UserInteractiveAuthInterceptor {
                                    override fun performStage(flowResponse: RegistrationFlowResponse, errCode: String?, promise: Continuation<UIABaseAuth>) {
                                        // We missed server grace period or it's not setup, see if we remember locally password
                                        if (flowResponse.nextUncompletedStage() == LoginFlowTypes.PASSWORD &&
                                                errCode == null &&
                                                reAuthHelper.data != null) {
                                            promise.resume(
                                                    UserPasswordAuth(
                                                            session = flowResponse.session,
                                                            user = session.myUserId,
                                                            password = reAuthHelper.data
                                                    )
                                            )
                                        } else {
                                            promise.resumeWithException(Exception("Cannot silently initialize cross signing, UIA missing"))
                                        }
                                    }
                                },
                                callback = it
                        )
                        Timber.d("Initialize cross signing SUCCESS")
                    }
                } catch (failure: Throwable) {
                    Timber.e(failure, "Failed to initialize cross signing")
                }
            }
        }
    }

    override fun handle(action: HomeActivityViewActions) {
        when (action) {
            HomeActivityViewActions.PushPromptHasBeenReviewed -> {
                vectorPreferences.setDidAskUserToEnableSessionPush()
            }
        }.exhaustive
    }
}
