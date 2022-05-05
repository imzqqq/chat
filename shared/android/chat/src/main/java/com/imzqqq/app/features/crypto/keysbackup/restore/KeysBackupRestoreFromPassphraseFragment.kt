package com.imzqqq.app.features.crypto.keysbackup.restore

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.text.set
import androidx.core.widget.doOnTextChanged
import com.imzqqq.app.R
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentKeysBackupRestoreFromPassphraseBinding
import javax.inject.Inject

class KeysBackupRestoreFromPassphraseFragment @Inject constructor() : VectorBaseFragment<FragmentKeysBackupRestoreFromPassphraseBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentKeysBackupRestoreFromPassphraseBinding {
        return FragmentKeysBackupRestoreFromPassphraseBinding.inflate(inflater, container, false)
    }

    private lateinit var viewModel: KeysBackupRestoreFromPassphraseViewModel
    private lateinit var sharedViewModel: KeysBackupRestoreSharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = fragmentViewModelProvider.get(KeysBackupRestoreFromPassphraseViewModel::class.java)
        sharedViewModel = activityViewModelProvider.get(KeysBackupRestoreSharedViewModel::class.java)

        viewModel.passphraseErrorText.observe(viewLifecycleOwner) { newValue ->
            views.keysBackupPassphraseEnterTil.error = newValue
        }

        views.helperTextWithLink.text = spannableStringForHelperText()

        views.keysBackupPassphraseEnterEdittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onRestoreBackup()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        views.helperTextWithLink.setOnClickListener { onUseRecoveryKey() }
        views.keysBackupRestoreWithPassphraseSubmit.setOnClickListener { onRestoreBackup() }
        views.keysBackupPassphraseEnterEdittext.doOnTextChanged { text, _, _, _ -> onPassphraseTextEditChange(text) }
    }

    private fun spannableStringForHelperText(): SpannableString {
        val clickableText = getString(R.string.keys_backup_restore_use_recovery_key)
        val helperText = getString(R.string.keys_backup_restore_with_passphrase_helper_with_link, clickableText)

        val spanString = SpannableString(helperText)

        // used just to have default link representation
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {}
        }
        val start = helperText.indexOf(clickableText)
        val end = start + clickableText.length
        spanString[start, end] = clickableSpan
        return spanString
    }

    private fun onPassphraseTextEditChange(s: CharSequence?) {
        s?.toString()?.let { viewModel.updatePassphrase(it) }
    }

    private fun onUseRecoveryKey() {
        sharedViewModel.moveToRecoverWithKey()
    }

    private fun onRestoreBackup() {
        val value = viewModel.passphrase.value
        if (value.isNullOrBlank()) {
            viewModel.passphraseErrorText.value = getString(R.string.passphrase_empty_error_message)
        } else {
            viewModel.recoverKeys(sharedViewModel)
        }
    }
}
