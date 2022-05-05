package com.imzqqq.app.features.settings.homeserver

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
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentGenericRecyclerBinding
import javax.inject.Inject

/**
 * Display some information about the homeserver
 */
class HomeserverSettingsFragment @Inject constructor(
        private val homeserverSettingsController: HomeserverSettingsController
) : VectorBaseFragment<FragmentGenericRecyclerBinding>(),
        HomeserverSettingsController.Callback {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericRecyclerBinding {
        return FragmentGenericRecyclerBinding.inflate(inflater, container, false)
    }

    private val viewModel: HomeserverSettingsViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeserverSettingsController.callback = this
        views.genericRecyclerView.configureWith(homeserverSettingsController)
    }

    override fun onDestroyView() {
        homeserverSettingsController.callback = null
        views.genericRecyclerView.cleanup()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.setTitle(R.string.settings_home_server)
    }

    override fun retry() {
        viewModel.handle(HomeserverSettingsAction.Refresh)
    }

    override fun invalidate() = withState(viewModel) { state ->
        homeserverSettingsController.setData(state)
    }
}
