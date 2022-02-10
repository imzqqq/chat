package com.imzqqq.app.features.login2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imzqqq.app.databinding.FragmentLoginResetPasswordSuccess2Binding
import javax.inject.Inject

/**
 * In this screen, we confirm to the user that his password has been reset
 */
class LoginResetPasswordSuccessFragment2 @Inject constructor() : AbstractLoginFragment2<FragmentLoginResetPasswordSuccess2Binding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginResetPasswordSuccess2Binding {
        return FragmentLoginResetPasswordSuccess2Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.resetPasswordSuccessSubmit.setOnClickListener { submit() }
    }

    private fun submit() {
        loginViewModel.handle(LoginAction2.PostViewEvent(LoginViewEvents2.OnResetPasswordMailConfirmationSuccessDone))
    }

    override fun resetViewModel() {
        loginViewModel.handle(LoginAction2.ResetResetPassword)
    }
}
