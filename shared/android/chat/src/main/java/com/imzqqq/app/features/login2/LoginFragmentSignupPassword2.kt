package com.imzqqq.app.features.login2

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.autofill.HintConstants
import androidx.lifecycle.lifecycleScope
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.extensions.hidePassword
import com.imzqqq.app.databinding.FragmentLoginSignupPassword2Binding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

/**
 * In this screen:
 * - the user is asked to choose a password to sign up to a homeserver.
 */
class LoginFragmentSignupPassword2 @Inject constructor() : AbstractLoginFragment2<FragmentLoginSignupPassword2Binding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginSignupPassword2Binding {
        return FragmentLoginSignupPassword2Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSubmitButton()
        setupAutoFill()

        views.passwordField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupAutoFill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            views.passwordField.setAutofillHints(HintConstants.AUTOFILL_HINT_NEW_PASSWORD)
        }
    }

    private fun submit() {
        cleanupUi()

        val password = views.passwordField.text.toString()

        // This can be called by the IME action, so deal with empty cases
        var error = 0
        if (password.isEmpty()) {
            views.passwordFieldTil.error = getString(R.string.error_empty_field_choose_password)
            error++
        }

        if (error == 0) {
            loginViewModel.handle(LoginAction2.SetUserPassword(password))
        }
    }

    private fun cleanupUi() {
        views.loginSubmit.hideKeyboard()
        views.passwordFieldTil.error = null
    }

    private fun setupSubmitButton() {
        views.loginSubmit.setOnClickListener { submit() }
        views.passwordField.textChanges()
                .onEach { password ->
                    views.passwordFieldTil.error = null
                    views.loginSubmit.isEnabled = password.isNotEmpty()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun resetViewModel() {
        // loginViewModel.handle(LoginAction2.ResetSignup)
    }

    override fun onError(throwable: Throwable) {
        views.passwordFieldTil.error = errorFormatter.toHumanReadable(throwable)
    }

    override fun updateWithState(state: LoginViewState2) {
        views.loginMatrixIdentifier.text = state.userIdentifier()

        if (state.isLoading) {
            // Ensure password is hidden
            views.passwordField.hidePassword()
        }
    }
}
