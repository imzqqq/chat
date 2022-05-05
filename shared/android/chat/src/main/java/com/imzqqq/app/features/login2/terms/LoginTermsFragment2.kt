package com.imzqqq.app.features.login2.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.args
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.toReducedUrl
import com.imzqqq.app.core.utils.openUrlInChromeCustomTab
import com.imzqqq.app.databinding.FragmentLoginTerms2Binding
import com.imzqqq.app.features.login.terms.LocalizedFlowDataLoginTermsChecked
import com.imzqqq.app.features.login.terms.LoginTermsFragmentArgument
import com.imzqqq.app.features.login.terms.LoginTermsViewState
import com.imzqqq.app.features.login.terms.PolicyController
import com.imzqqq.app.features.login2.AbstractLoginFragment2
import com.imzqqq.app.features.login2.LoginAction2
import com.imzqqq.app.features.login2.LoginViewState2
import org.matrix.android.sdk.internal.auth.registration.LocalizedFlowDataLoginTerms
import javax.inject.Inject

/**
 * LoginTermsFragment displays the list of policies the user has to accept
 */
class LoginTermsFragment2 @Inject constructor(
        private val policyController: PolicyController
) : AbstractLoginFragment2<FragmentLoginTerms2Binding>(),
        PolicyController.PolicyControllerListener {

    private val params: LoginTermsFragmentArgument by args()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginTerms2Binding {
        return FragmentLoginTerms2Binding.inflate(inflater, container, false)
    }

    private var loginTermsViewState: LoginTermsViewState = LoginTermsViewState(emptyList())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        views.loginTermsPolicyList.configureWith(policyController)
        policyController.listener = this

        val list = ArrayList<LocalizedFlowDataLoginTermsChecked>()

        params.localizedFlowDataLoginTerms
                .forEach {
                    list.add(LocalizedFlowDataLoginTermsChecked(it))
                }

        loginTermsViewState = LoginTermsViewState(list)
    }

    private fun setupViews() {
        views.loginTermsSubmit.setOnClickListener { submit() }
    }

    override fun onDestroyView() {
        views.loginTermsPolicyList.cleanup()
        policyController.listener = null
        super.onDestroyView()
    }

    private fun renderState() {
        policyController.setData(loginTermsViewState.localizedFlowDataLoginTermsChecked)

        // Button is enabled only if all checkboxes are checked
        views.loginTermsSubmit.isEnabled = loginTermsViewState.allChecked()
    }

    override fun setChecked(localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms, isChecked: Boolean) {
        if (isChecked) {
            loginTermsViewState.check(localizedFlowDataLoginTerms)
        } else {
            loginTermsViewState.uncheck(localizedFlowDataLoginTerms)
        }

        renderState()
    }

    override fun openPolicy(localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms) {
        localizedFlowDataLoginTerms.localizedUrl
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    openUrlInChromeCustomTab(requireContext(), null, it)
                }
    }

    private fun submit() {
        loginViewModel.handle(LoginAction2.AcceptTerms)
    }

    override fun updateWithState(state: LoginViewState2) {
        policyController.homeServer = state.homeServerUrlFromUser.toReducedUrl()
        renderState()
    }

    override fun resetViewModel() {
        loginViewModel.handle(LoginAction2.ResetSignup)
    }
}
