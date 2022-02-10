package com.imzqqq.app.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.extensions.hidePassword
import com.imzqqq.app.core.extensions.isEmail
import com.imzqqq.app.core.extensions.toReducedUrl
import com.imzqqq.app.databinding.FragmentLoginResetPasswordBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

/**
 * In this screen, the user is asked for email and new password to reset his password
 */
class LoginResetPasswordFragment @Inject constructor() : AbstractLoginFragment<FragmentLoginResetPasswordBinding>() {

    // Show warning only once
    private var showWarning = true

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginResetPasswordBinding {
        return FragmentLoginResetPasswordBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSubmitButton()
    }

    private fun setupUi(state: LoginViewState) {
        views.resetPasswordTitle.text = getString(R.string.login_reset_password_on, state.homeServerUrlFromUser.toReducedUrl())
    }

    private fun setupSubmitButton() {
        views.resetPasswordSubmit.setOnClickListener { submit() }
        combine(
                views.resetPasswordEmail.textChanges().map { it.isEmail() },
                views.passwordField.textChanges().map { it.isNotEmpty() }
        ) { isEmail, isPasswordNotEmpty ->
            isEmail && isPasswordNotEmpty
        }
                .onEach {
                    views.resetPasswordEmailTil.error = null
                    views.passwordFieldTil.error = null
                    views.resetPasswordSubmit.isEnabled = it
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun submit() {
        cleanupUi()

        if (showWarning) {
            showWarning = false
            // Display a warning as Riot-Web does first
            MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.login_reset_password_warning_title)
                    .setMessage(R.string.login_reset_password_warning_content)
                    .setPositiveButton(R.string.login_reset_password_warning_submit) { _, _ ->
                        doSubmit()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
        } else {
            doSubmit()
        }
    }

    private fun doSubmit() {
        val email = views.resetPasswordEmail.text.toString()
        val password = views.passwordField.text.toString()

        loginViewModel.handle(LoginAction.ResetPassword(email, password))
    }

    private fun cleanupUi() {
        views.resetPasswordSubmit.hideKeyboard()
        views.resetPasswordEmailTil.error = null
        views.passwordFieldTil.error = null
    }

    override fun resetViewModel() {
        loginViewModel.handle(LoginAction.ResetResetPassword)
    }

    override fun updateWithState(state: LoginViewState) {
        setupUi(state)

        when (state.asyncResetPassword) {
            is Loading -> {
                // Ensure new password is hidden
                views.passwordField.hidePassword()
            }
            is Fail    -> {
                views.resetPasswordEmailTil.error = errorFormatter.toHumanReadable(state.asyncResetPassword.error)
            }
            is Success -> Unit
        }
    }
}
