package com.imzqqq.app.features.settings.threepids

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.extensions.getFormattedValue
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.extensions.isEmail
import com.imzqqq.app.core.extensions.isMsisdn
import com.imzqqq.app.core.extensions.registerStartForActivityResult
import com.imzqqq.app.core.platform.OnBackPressed
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import com.imzqqq.app.features.auth.ReAuthActivity
import org.matrix.android.sdk.api.auth.data.LoginFlowTypes
import org.matrix.android.sdk.api.session.identity.ThreePid
import javax.inject.Inject

class ThreePidsSettingsFragment @Inject constructor(
        private val epoxyController: ThreePidsSettingsController
) :
        VectorBaseFragment<FragmentGenericRecyclerBinding>(),
        OnBackPressed,
        ThreePidsSettingsController.InteractionListener {

    private val viewModel: ThreePidsSettingsViewModel by fragmentViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericRecyclerBinding {
        return FragmentGenericRecyclerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.genericRecyclerView.configureWith(epoxyController)
        epoxyController.interactionListener = this

        viewModel.observeViewEvents {
            when (it) {
                is ThreePidsSettingsViewEvents.Failure -> displayErrorDialog(it.throwable)
                is ThreePidsSettingsViewEvents.RequestReAuth -> askAuthentication(it)
            }.exhaustive
        }
    }

    private fun askAuthentication(event: ThreePidsSettingsViewEvents.RequestReAuth) {
        ReAuthActivity.newIntent(requireContext(),
                event.registrationFlowResponse,
                event.lastErrorCode,
                getString(R.string.settings_add_email_address)).let { intent ->
            reAuthActivityResultLauncher.launch(intent)
        }
    }
    private val reAuthActivityResultLauncher = registerStartForActivityResult { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            when (activityResult.data?.extras?.getString(ReAuthActivity.RESULT_FLOW_TYPE)) {
                LoginFlowTypes.SSO -> {
                    viewModel.handle(ThreePidsSettingsAction.SsoAuthDone)
                }
                LoginFlowTypes.PASSWORD -> {
                    val password = activityResult.data?.extras?.getString(ReAuthActivity.RESULT_VALUE) ?: ""
                    viewModel.handle(ThreePidsSettingsAction.PasswordAuthDone(password))
                }
                else                    -> {
                    viewModel.handle(ThreePidsSettingsAction.ReAuthCancelled)
                }
            }
        } else {
            viewModel.handle(ThreePidsSettingsAction.ReAuthCancelled)
        }
    }

    override fun onDestroyView() {
        views.genericRecyclerView.cleanup()
        epoxyController.interactionListener = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.setTitle(R.string.settings_emails_and_phone_numbers_title)
    }

    override fun invalidate() = withState(viewModel) { state ->
        if (state.isLoading) {
            showLoadingDialog()
        } else {
            dismissLoadingDialog()
        }
        epoxyController.setData(state)
    }

    override fun addEmail() {
        viewModel.handle(ThreePidsSettingsAction.ChangeUiState(ThreePidsSettingsUiState.AddingEmail(null)))
    }

    override fun doAddEmail(email: String) {
        // Sanity
        val safeEmail = email.trim().replace(" ", "")
        viewModel.handle(ThreePidsSettingsAction.ChangeUiState(ThreePidsSettingsUiState.AddingEmail(null)))

        // Check that email is valid
        if (!safeEmail.isEmail()) {
            viewModel.handle(ThreePidsSettingsAction.ChangeUiState(ThreePidsSettingsUiState.AddingEmail(getString(R.string.auth_invalid_email))))
            return
        }

        viewModel.handle(ThreePidsSettingsAction.AddThreePid(ThreePid.Email(safeEmail)))
    }

    override fun addMsisdn() {
        viewModel.handle(ThreePidsSettingsAction.ChangeUiState(ThreePidsSettingsUiState.AddingPhoneNumber(null)))
    }

    override fun doAddMsisdn(msisdn: String) {
        // Sanity
        val safeMsisdn = msisdn.trim().replace(" ", "")

        viewModel.handle(ThreePidsSettingsAction.ChangeUiState(ThreePidsSettingsUiState.AddingPhoneNumber(null)))

        // Check that phone number is valid
        if (!msisdn.startsWith("+")) {
            viewModel.handle(
                    ThreePidsSettingsAction.ChangeUiState(ThreePidsSettingsUiState.AddingPhoneNumber(getString(R.string.login_msisdn_error_not_international)))
            )
            return
        }

        if (!msisdn.isMsisdn()) {
            viewModel.handle(
                    ThreePidsSettingsAction.ChangeUiState(ThreePidsSettingsUiState.AddingPhoneNumber(getString(R.string.login_msisdn_error_other)))
            )
            return
        }

        viewModel.handle(ThreePidsSettingsAction.AddThreePid(ThreePid.Msisdn(safeMsisdn)))
    }

    override fun submitCode(threePid: ThreePid.Msisdn, code: String) {
        viewModel.handle(ThreePidsSettingsAction.SubmitCode(threePid, code))
        // Hide the keyboard
        view?.hideKeyboard()
    }

    override fun cancelAdding() {
        viewModel.handle(ThreePidsSettingsAction.ChangeUiState(ThreePidsSettingsUiState.Idle))
        // Hide the keyboard
        view?.hideKeyboard()
    }

    override fun continueThreePid(threePid: ThreePid) {
        viewModel.handle(ThreePidsSettingsAction.ContinueThreePid(threePid))
    }

    override fun cancelThreePid(threePid: ThreePid) {
        viewModel.handle(ThreePidsSettingsAction.CancelThreePid(threePid))
    }

    override fun deleteThreePid(threePid: ThreePid) {
        MaterialAlertDialogBuilder(requireActivity(), R.style.ThemeOverlay_Vector_MaterialAlertDialog_Destructive)
                .setMessage(getString(R.string.settings_remove_three_pid_confirmation_content, threePid.getFormattedValue()))
                .setPositiveButton(R.string.remove) { _, _ ->
                    viewModel.handle(ThreePidsSettingsAction.DeleteThreePid(threePid))
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    override fun onBackPressed(toolbarButton: Boolean): Boolean {
        return withState(viewModel) {
            if (it.uiState is ThreePidsSettingsUiState.Idle) {
                false
            } else {
                cancelAdding()
                true
            }
        }
    }
}
