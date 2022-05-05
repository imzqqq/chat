package com.imzqqq.app.features.crypto.recover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentBootstrapSetupRecoveryBinding
import javax.inject.Inject

class BootstrapSetupRecoveryKeyFragment @Inject constructor() :
    VectorBaseFragment<FragmentBootstrapSetupRecoveryBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBootstrapSetupRecoveryBinding {
        return FragmentBootstrapSetupRecoveryBinding.inflate(inflater, container, false)
    }

    val sharedViewModel: BootstrapSharedViewModel by parentFragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Actions when a key backup exist
        views.bootstrapSetupSecureSubmit.views.bottomSheetActionClickableZone.debouncedClicks {
            sharedViewModel.handle(BootstrapActions.StartKeyBackupMigration)
        }

        // Actions when there is no key backup
        views.bootstrapSetupSecureUseSecurityKey.views.bottomSheetActionClickableZone.debouncedClicks {
            sharedViewModel.handle(BootstrapActions.Start(userWantsToEnterPassphrase = false))
        }
        views.bootstrapSetupSecureUseSecurityPassphrase.views.bottomSheetActionClickableZone.debouncedClicks {
            sharedViewModel.handle(BootstrapActions.Start(userWantsToEnterPassphrase = true))
        }
    }

    override fun invalidate() = withState(sharedViewModel) { state ->
        if (state.step is BootstrapStep.FirstForm) {
            if (state.step.keyBackUpExist) {
                // Display the set up action
                views.bootstrapSetupSecureSubmit.isVisible = true
                views.bootstrapSetupSecureUseSecurityKey.isVisible = false
                views.bootstrapSetupSecureUseSecurityPassphrase.isVisible = false
                views.bootstrapSetupSecureUseSecurityPassphraseSeparator.isVisible = false
            } else {
                if (state.step.reset) {
                    views.bootstrapSetupSecureText.text = getString(R.string.reset_secure_backup_title)
                    views.bootstrapSetupWarningTextView.isVisible = true
                } else {
                    views.bootstrapSetupSecureText.text = getString(R.string.bottom_sheet_setup_secure_backup_subtitle)
                    views.bootstrapSetupWarningTextView.isVisible = false
                }
                // Choose between create a passphrase or use a recovery key
                views.bootstrapSetupSecureSubmit.isVisible = false
                views.bootstrapSetupSecureUseSecurityKey.isVisible = true
                views.bootstrapSetupSecureUseSecurityPassphrase.isVisible = true
                views.bootstrapSetupSecureUseSecurityPassphraseSeparator.isVisible = true
            }
        }
    }
}
