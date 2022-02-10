package com.imzqqq.app.features.crypto.quads

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.activityViewModel
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.registerStartForActivityResult
import com.imzqqq.app.core.flow.throttleFirst
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.startImportTextFromFileIntent
import com.imzqqq.app.databinding.FragmentSsssAccessFromKeyBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.matrix.android.sdk.api.extensions.tryOrNull
import reactivecircus.flowbinding.android.widget.editorActionEvents
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

class SharedSecuredStorageKeyFragment @Inject constructor() : VectorBaseFragment<FragmentSsssAccessFromKeyBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSsssAccessFromKeyBinding {
        return FragmentSsssAccessFromKeyBinding.inflate(inflater, container, false)
    }

    val sharedViewModel: SharedSecureStorageViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.ssssRestoreWithKeyText.text = getString(R.string.enter_secret_storage_input_key)

        views.ssssKeyEnterEdittext.editorActionEvents()
                .throttleFirst(300)
                .onEach {
                    if (it.actionId == EditorInfo.IME_ACTION_DONE) {
                        submit()
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

        views.ssssKeyEnterEdittext.textChanges()
                .skipInitialValue()
                .onEach {
                    views.ssssKeyEnterTil.error = null
                    views.ssssKeySubmit.isEnabled = it.isNotBlank()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

        views.ssssKeyUseFile.debouncedClicks { startImportTextFromFileIntent(requireContext(), importFileStartForActivityResult) }

        views.ssssKeyReset.views.bottomSheetActionClickableZone.debouncedClicks {
            sharedViewModel.handle(SharedSecureStorageAction.ForgotResetAll)
        }

        sharedViewModel.observeViewEvents {
            when (it) {
                is SharedSecureStorageViewEvent.KeyInlineError -> {
                    views.ssssKeyEnterTil.error = it.message
                }
            }
        }

        views.ssssKeySubmit.debouncedClicks { submit() }
    }

    fun submit() {
        val text = views.ssssKeyEnterEdittext.text.toString()
        if (text.isBlank()) return // Should not reach this point as button disabled
        views.ssssKeySubmit.isEnabled = false
        sharedViewModel.handle(SharedSecureStorageAction.SubmitKey(text))
    }

    private val importFileStartForActivityResult = registerStartForActivityResult { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            activityResult.data?.data?.let { dataURI ->
                tryOrNull {
                    activity?.contentResolver?.openInputStream(dataURI)
                            ?.bufferedReader()
                            ?.use { it.readText() }
                            ?.let {
                                views.ssssKeyEnterEdittext.setText(it)
                            }
                }
            }
        }
    }
}
