package com.imzqqq.app.features.settings.locale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.extensions.restart
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentLocalePickerBinding
import java.util.Locale
import javax.inject.Inject

class LocalePickerFragment @Inject constructor(
        private val controller: LocalePickerController
) : VectorBaseFragment<FragmentLocalePickerBinding>(),
        LocalePickerController.Listener {

    private val viewModel: LocalePickerViewModel by fragmentViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLocalePickerBinding {
        return FragmentLocalePickerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.localeRecyclerView.configureWith(controller)
        controller.listener = this

        viewModel.observeViewEvents {
            when (it) {
                LocalePickerViewEvents.RestartActivity -> {
                    activity?.restart()
                }
            }.exhaustive
        }
    }

    override fun onDestroyView() {
        views.localeRecyclerView.cleanup()
        controller.listener = null
        super.onDestroyView()
    }

    override fun invalidate() = withState(viewModel) { state ->
        controller.setData(state)
    }

    override fun onUseCurrentClicked() {
        // Just close the fragment
        parentFragmentManager.popBackStack()
    }

    override fun onLocaleClicked(locale: Locale) {
        viewModel.handle(LocalePickerAction.SelectLocale(locale))
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.setTitle(R.string.settings_select_language)
    }
}
