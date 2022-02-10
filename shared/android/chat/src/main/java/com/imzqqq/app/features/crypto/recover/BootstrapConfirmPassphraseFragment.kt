package com.imzqqq.app.features.crypto.recover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.flow.throttleFirst
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentBootstrapEnterPassphraseBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.editorActionEvents
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

class BootstrapConfirmPassphraseFragment @Inject constructor() :
    VectorBaseFragment<FragmentBootstrapEnterPassphraseBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBootstrapEnterPassphraseBinding {
        return FragmentBootstrapEnterPassphraseBinding.inflate(inflater, container, false)
    }

    val sharedViewModel: BootstrapSharedViewModel by parentFragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.ssssPassphraseSecurityProgress.isGone = true

        views.bootstrapDescriptionText.text = getString(R.string.set_a_security_phrase_again_notice)
        views.ssssPassphraseEnterEdittext.hint = getString(R.string.set_a_security_phrase_hint)

        withState(sharedViewModel) {
            // set initial value (useful when coming back)
            views.ssssPassphraseEnterEdittext.setText(it.passphraseRepeat ?: "")
            views.ssssPassphraseEnterEdittext.requestFocus()
        }

        views.ssssPassphraseEnterEdittext.editorActionEvents()
                .throttleFirst(300)
                .onEach {
                    if (it.actionId == EditorInfo.IME_ACTION_DONE) {
                        submit()
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

        views.ssssPassphraseEnterEdittext.textChanges()
                .onEach {
                    views.ssssPassphraseEnterTil.error = null
                    sharedViewModel.handle(BootstrapActions.UpdateConfirmCandidatePassphrase(it.toString()))
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

        sharedViewModel.observeViewEvents {
            //            when (it) {
//                is SharedSecureStorageViewEvent.InlineError -> {
//                    ssss_passphrase_enter_til.error = it.message
//                }
//            }
        }

        views.bootstrapSubmit.debouncedClicks { submit() }
    }

    private fun submit() = withState(sharedViewModel) { state ->
        if (state.step !is BootstrapStep.ConfirmPassphrase) {
            return@withState
        }
        val passphrase = views.ssssPassphraseEnterEdittext.text?.toString()
        when {
            passphrase.isNullOrBlank()     ->
                views.ssssPassphraseEnterTil.error = getString(R.string.passphrase_empty_error_message)
            passphrase != state.passphrase ->
                views.ssssPassphraseEnterTil.error = getString(R.string.passphrase_passphrase_does_not_match)
            else                           -> {
                view?.hideKeyboard()
                sharedViewModel.handle(BootstrapActions.DoInitialize(passphrase))
            }
        }
    }
}
