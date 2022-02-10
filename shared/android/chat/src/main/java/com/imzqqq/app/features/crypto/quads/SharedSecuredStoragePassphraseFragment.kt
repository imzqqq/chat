package com.imzqqq.app.features.crypto.quads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.text.toSpannable
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.activityViewModel
import com.imzqqq.app.R
import com.imzqqq.app.core.flow.throttleFirst
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.databinding.FragmentSsssAccessFromPassphraseBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.editorActionEvents
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

class SharedSecuredStoragePassphraseFragment @Inject constructor(
        private val colorProvider: ColorProvider
) : VectorBaseFragment<FragmentSsssAccessFromPassphraseBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSsssAccessFromPassphraseBinding {
        return FragmentSsssAccessFromPassphraseBinding.inflate(inflater, container, false)
    }

    val sharedViewModel: SharedSecureStorageViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If has passphrase
        val pass = getString(R.string.recovery_passphrase)
        val key = getString(R.string.recovery_key)
        views.ssssRestoreWithPassphraseWarningText.text = getString(
                R.string.enter_secret_storage_passphrase_or_key,
                pass,
                key
        )
                .toSpannable()
        // TODO Restore coloration when we will have a FAQ to open with those terms
        // .colorizeMatchingText(pass, colorProvider.getColorFromAttribute(android.R.attr.textColorLink))
        // .colorizeMatchingText(key, colorProvider.getColorFromAttribute(android.R.attr.textColorLink))

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
                    views.ssssPassphraseSubmit.isEnabled = it.isNotBlank()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

        views.ssssPassphraseReset.views.bottomSheetActionClickableZone.debouncedClicks {
            sharedViewModel.handle(SharedSecureStorageAction.ForgotResetAll)
        }

        sharedViewModel.observeViewEvents {
            when (it) {
                is SharedSecureStorageViewEvent.InlineError -> {
                    views.ssssPassphraseEnterTil.error = it.message
                }
            }
        }

        views.ssssPassphraseSubmit.debouncedClicks { submit() }
        views.ssssPassphraseUseKey.debouncedClicks { sharedViewModel.handle(SharedSecureStorageAction.UseKey) }
    }

    fun submit() {
        val text = views.ssssPassphraseEnterEdittext.text.toString()
        if (text.isBlank()) return // Should not reach this point as button disabled
        views.ssssPassphraseSubmit.isEnabled = false
        sharedViewModel.handle(SharedSecureStorageAction.SubmitPassphrase(text))
    }
}
