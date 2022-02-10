package com.imzqqq.app.features.signout.soft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import com.imzqqq.app.features.login.AbstractLoginFragment
import com.imzqqq.app.features.login.LoginAction
import com.imzqqq.app.features.login.LoginMode
import com.imzqqq.app.features.login.LoginViewEvents
import javax.inject.Inject

/**
 * In this screen:
 * - the user is asked to enter a password to sign in again to a homeserver.
 * - or to cleanup all the data
 */
class SoftLogoutFragment @Inject constructor(
        private val softLogoutController: SoftLogoutController
) : AbstractLoginFragment<FragmentGenericRecyclerBinding>(),
        SoftLogoutController.Listener {

    private val softLogoutViewModel: SoftLogoutViewModel by activityViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericRecyclerBinding {
        return FragmentGenericRecyclerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        softLogoutViewModel.onEach { softLogoutViewState ->
            softLogoutController.update(softLogoutViewState)
            when (val mode = softLogoutViewState.asyncHomeServerLoginFlowRequest.invoke()) {
                is LoginMode.SsoAndPassword -> {
                    loginViewModel.handle(LoginAction.SetupSsoForSessionRecovery(
                            softLogoutViewState.homeServerUrl,
                            softLogoutViewState.deviceId,
                            mode.ssoIdentityProviders
                    ))
                }
                is LoginMode.Sso -> {
                    loginViewModel.handle(LoginAction.SetupSsoForSessionRecovery(
                            softLogoutViewState.homeServerUrl,
                            softLogoutViewState.deviceId,
                            mode.ssoIdentityProviders
                    ))
                }
                LoginMode.Unsupported -> {
                    // Prepare the loginViewModel for a SSO/login fallback recovery
                    loginViewModel.handle(LoginAction.SetupSsoForSessionRecovery(
                            softLogoutViewState.homeServerUrl,
                            softLogoutViewState.deviceId,
                            null
                    ))
                }
                else                  -> Unit
            }
        }
    }

    private fun setupRecyclerView() {
        views.genericRecyclerView.configureWith(softLogoutController)
        softLogoutController.listener = this
    }

    override fun onDestroyView() {
        views.genericRecyclerView.cleanup()
        softLogoutController.listener = null
        super.onDestroyView()
    }

    override fun retry() {
        softLogoutViewModel.handle(SoftLogoutAction.RetryLoginFlow)
    }

    override fun passwordEdited(password: String) {
        softLogoutViewModel.handle(SoftLogoutAction.PasswordChanged(password))
    }

    override fun submit() = withState(softLogoutViewModel) { state ->
        cleanupUi()
        softLogoutViewModel.handle(SoftLogoutAction.SignInAgain(state.enteredPassword))
    }

    override fun signinFallbackSubmit() = withState(loginViewModel) { state ->
        // The loginViewModel has been prepared for a SSO/login fallback recovery (above)
        loginViewModel.handle(LoginAction.PostViewEvent(LoginViewEvents.OnSignModeSelected(state.signMode)))
    }

    override fun clearData() {
        withState(softLogoutViewModel) { state ->
            cleanupUi()

            val messageResId = if (state.hasUnsavedKeys) {
                R.string.soft_logout_clear_data_dialog_e2e_warning_content
            } else {
                R.string.soft_logout_clear_data_dialog_content
            }

            MaterialAlertDialogBuilder(requireActivity(), R.style.ThemeOverlay_Vector_MaterialAlertDialog_Destructive)
                    .setTitle(R.string.soft_logout_clear_data_dialog_title)
                    .setMessage(messageResId)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.soft_logout_clear_data_submit) { _, _ ->
                        softLogoutViewModel.handle(SoftLogoutAction.ClearData)
                    }
                    .show()
        }
    }

    private fun cleanupUi() {
        views.genericRecyclerView.hideKeyboard()
    }

    override fun forgetPasswordClicked() {
        loginViewModel.handle(LoginAction.PostViewEvent(LoginViewEvents.OnForgetPasswordClicked))
    }

    override fun resetViewModel() {
        // No op
    }
}
