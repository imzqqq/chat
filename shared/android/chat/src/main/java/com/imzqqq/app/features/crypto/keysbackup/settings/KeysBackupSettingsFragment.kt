package com.imzqqq.app.features.crypto.keysbackup.settings

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
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentKeysBackupSettingsBinding
import com.imzqqq.app.features.crypto.keysbackup.restore.KeysBackupRestoreActivity
import com.imzqqq.app.features.crypto.keysbackup.setup.KeysBackupSetupActivity
import javax.inject.Inject

class KeysBackupSettingsFragment @Inject constructor(private val keysBackupSettingsRecyclerViewController: KeysBackupSettingsRecyclerViewController) :
    VectorBaseFragment<FragmentKeysBackupSettingsBinding>(),
        KeysBackupSettingsRecyclerViewController.Listener {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentKeysBackupSettingsBinding {
        return FragmentKeysBackupSettingsBinding.inflate(inflater, container, false)
    }

    private val viewModel: KeysBackupSettingsViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.keysBackupSettingsRecyclerView.configureWith(keysBackupSettingsRecyclerViewController)
        keysBackupSettingsRecyclerViewController.listener = this
    }

    override fun onDestroyView() {
        keysBackupSettingsRecyclerViewController.listener = null
        views.keysBackupSettingsRecyclerView.cleanup()
        super.onDestroyView()
    }

    override fun invalidate() = withState(viewModel) { state ->
        keysBackupSettingsRecyclerViewController.setData(state)
    }

    override fun didSelectSetupMessageRecovery() {
        context?.let {
            startActivity(KeysBackupSetupActivity.intent(it, false))
        }
    }

    override fun didSelectRestoreMessageRecovery() {
        context?.let {
            startActivity(KeysBackupRestoreActivity.intent(it))
        }
    }

    override fun didSelectDeleteSetupMessageRecovery() {
        activity?.let {
            MaterialAlertDialogBuilder(it)
                    .setTitle(R.string.keys_backup_settings_delete_confirm_title)
                    .setMessage(R.string.keys_backup_settings_delete_confirm_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.keys_backup_settings_delete_confirm_title) { _, _ ->
                        viewModel.handle(KeyBackupSettingsAction.DeleteKeyBackup)
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .setCancelable(true)
                    .show()
        }
    }

    override fun loadTrustData() {
        viewModel.handle(KeyBackupSettingsAction.GetKeyBackupTrust)
    }

    override fun loadKeysBackupState() {
        viewModel.handle(KeyBackupSettingsAction.Init)
    }
}
