package com.imzqqq.app.features.crypto.keysbackup.restore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.imzqqq.app.R
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.LiveEvent
import com.imzqqq.app.databinding.FragmentKeysBackupRestoreSuccessBinding
import javax.inject.Inject

class KeysBackupRestoreSuccessFragment @Inject constructor() : VectorBaseFragment<FragmentKeysBackupRestoreSuccessBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentKeysBackupRestoreSuccessBinding {
        return FragmentKeysBackupRestoreSuccessBinding.inflate(inflater, container, false)
    }

    private lateinit var sharedViewModel: KeysBackupRestoreSharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = activityViewModelProvider.get(KeysBackupRestoreSharedViewModel::class.java)

        if (compareValues(sharedViewModel.importKeyResult?.totalNumberOfKeys, 0) > 0) {
            sharedViewModel.importKeyResult?.let {
                val part1 = resources.getQuantityString(R.plurals.keys_backup_restore_success_description_part1,
                        it.totalNumberOfKeys, it.totalNumberOfKeys)
                val part2 = resources.getQuantityString(R.plurals.keys_backup_restore_success_description_part2,
                        it.successfullyNumberOfImportedKeys, it.successfullyNumberOfImportedKeys)
                views.successDetailsText.text = String.format("%s\n%s", part1, part2)
            }
            // We don't put emoji in string xml as it will crash on old devices
            views.successText.text = context?.getString(R.string.keys_backup_restore_success_title, "🎉")
        } else {
            views.successText.text = context?.getString(R.string.keys_backup_restore_success_title_already_up_to_date)
            views.successDetailsText.isVisible = false
        }
        views.keysBackupSetupDoneButton.setOnClickListener { onDone() }
    }

    private fun onDone() {
        sharedViewModel.importRoomKeysFinishWithResult.value = LiveEvent(sharedViewModel.importKeyResult!!)
    }
}
