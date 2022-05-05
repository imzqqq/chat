package com.imzqqq.app.features.login

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.args
import com.imzqqq.app.R
import com.imzqqq.app.databinding.FragmentLoginWaitForEmailBinding
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.failure.is401
import javax.inject.Inject

@Parcelize
data class LoginWaitForEmailFragmentArgument(
        val email: String
) : Parcelable

/**
 * In this screen, the user is asked to check his emails
 */
class LoginWaitForEmailFragment @Inject constructor() : AbstractLoginFragment<FragmentLoginWaitForEmailBinding>() {

    private val params: LoginWaitForEmailFragmentArgument by args()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginWaitForEmailBinding {
        return FragmentLoginWaitForEmailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
    }

    override fun onResume() {
        super.onResume()

        loginViewModel.handle(LoginAction.CheckIfEmailHasBeenValidated(0))
    }

    override fun onPause() {
        super.onPause()

        loginViewModel.handle(LoginAction.StopEmailValidationCheck)
    }

    private fun setupUi() {
        views.loginWaitForEmailNotice.text = getString(R.string.login_wait_for_email_notice, params.email)
    }

    override fun onError(throwable: Throwable) {
        if (throwable.is401()) {
            // Try again, with a delay
            loginViewModel.handle(LoginAction.CheckIfEmailHasBeenValidated(10_000))
        } else {
            super.onError(throwable)
        }
    }

    override fun resetViewModel() {
        loginViewModel.handle(LoginAction.ResetLogin)
    }
}
