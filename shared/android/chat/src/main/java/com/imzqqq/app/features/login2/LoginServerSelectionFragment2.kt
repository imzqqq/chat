package com.imzqqq.app.features.login2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.setTextWithColoredPart
import com.imzqqq.app.core.utils.openUrlInChromeCustomTab
import com.imzqqq.app.databinding.FragmentLoginServerSelection2Binding
import com.imzqqq.app.features.login.EMS_LINK
import javax.inject.Inject

/**
 * In this screen, the user will choose between matrix.org, or other type of homeserver
 */
class LoginServerSelectionFragment2 @Inject constructor() : AbstractLoginFragment2<FragmentLoginServerSelection2Binding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginServerSelection2Binding {
        return FragmentLoginServerSelection2Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        views.loginServerChoiceMatrixOrg.setOnClickListener { selectMatrixOrg() }
        views.loginServerChoiceOther.setOnClickListener { selectOther() }

        views.loginServerChoiceEmsLearnMore.setTextWithColoredPart(
                fullTextRes = R.string.login_server_modular_learn_more_about_ems,
                coloredTextRes = R.string.login_server_modular_learn_more,
                underline = true
        )
        views.loginServerChoiceEmsLearnMore.setOnClickListener {
            openUrlInChromeCustomTab(requireActivity(), null, EMS_LINK)
        }
    }

    private fun updateUi(state: LoginViewState2) {
        when (state.signMode) {
            SignMode2.Unknown -> Unit
            SignMode2.SignUp  -> {
                views.loginServerTitle.setText(R.string.login_please_choose_a_server)
            }
            SignMode2.SignIn  -> {
                views.loginServerTitle.setText(R.string.login_please_select_your_server)
            }
        }
    }

    private fun selectMatrixOrg() {
        views.loginServerChoiceMatrixOrg.isChecked = true
        loginViewModel.handle(LoginAction2.ChooseDefaultHomeServer)
    }

    private fun selectOther() {
        views.loginServerChoiceOther.isChecked = true
        loginViewModel.handle(LoginAction2.EnterServerUrl)
    }

    override fun onResume() {
        super.onResume()
        views.loginServerChoiceMatrixOrg.isChecked = false
        views.loginServerChoiceOther.isChecked = false
    }

    override fun resetViewModel() {
        loginViewModel.handle(LoginAction2.ResetHomeServerUrl)
    }

    override fun updateWithState(state: LoginViewState2) {
        updateUi(state)
    }
}
