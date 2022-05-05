package com.imzqqq.app.features.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.BuildConfig
import com.imzqqq.app.R
import com.imzqqq.app.databinding.FragmentLoginSplashBinding
import com.imzqqq.app.features.settings.VectorPreferences
import org.matrix.android.sdk.api.failure.Failure
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * In this screen, the user is viewing an introduction to what he can do with this application
 */
class LoginSplashFragment @Inject constructor(
        private val vectorPreferences: VectorPreferences
) : AbstractLoginFragment<FragmentLoginSplashBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginSplashBinding {
        return FragmentLoginSplashBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        views.loginSplashSubmit.debouncedClicks { getStarted() }

        if (BuildConfig.DEBUG || vectorPreferences.developerMode()) {
            views.loginSplashVersion.isVisible = true
            @SuppressLint("SetTextI18n")
            views.loginSplashVersion.text = "Version : ${BuildConfig.VERSION_NAME}\n" +
                    "Branch: ${BuildConfig.GIT_BRANCH_NAME}\n" +
                    "Build: ${BuildConfig.BUILD_NUMBER}"
        }
    }

    private fun getStarted() {
        loginViewModel.handle(LoginAction.OnGetStarted(resetLoginConfig = false))
    }

    override fun resetViewModel() {
        // Nothing to do
    }

    override fun onError(throwable: Throwable) {
        if (throwable is Failure.NetworkConnection &&
                throwable.ioException is UnknownHostException) {
            // Invalid homeserver from URL config
            val url = loginViewModel.getInitialHomeServerUrl().orEmpty()
            MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.dialog_title_error)
                    .setMessage(getString(R.string.login_error_homeserver_from_url_not_found, url))
                    .setPositiveButton(R.string.login_error_homeserver_from_url_not_found_enter_manual) { _, _ ->
                        loginViewModel.handle(LoginAction.OnGetStarted(resetLoginConfig = true))
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
        } else {
            super.onError(throwable)
        }
    }
}
