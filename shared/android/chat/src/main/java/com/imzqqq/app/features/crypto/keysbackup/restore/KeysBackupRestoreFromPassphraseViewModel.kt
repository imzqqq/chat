package com.imzqqq.app.features.crypto.keysbackup.restore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.StringProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class KeysBackupRestoreFromPassphraseViewModel @Inject constructor(
        private val stringProvider: StringProvider
) : ViewModel() {

    var passphrase: MutableLiveData<String?> = MutableLiveData(null)
    var passphraseErrorText: MutableLiveData<String?> = MutableLiveData(null)

    // ========= Actions =========

    fun updatePassphrase(newValue: String) {
        passphrase.value = newValue
        passphraseErrorText.value = null
    }

    fun recoverKeys(sharedViewModel: KeysBackupRestoreSharedViewModel) {
        passphraseErrorText.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sharedViewModel.recoverUsingBackupPass(passphrase.value!!)
            } catch (failure: Throwable) {
                passphraseErrorText.postValue(stringProvider.getString(R.string.keys_backup_passphrase_error_decrypt))
            }
        }
    }
}
