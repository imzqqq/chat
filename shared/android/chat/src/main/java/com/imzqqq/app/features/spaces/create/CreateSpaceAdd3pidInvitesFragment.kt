package com.imzqqq.app.features.spaces.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.platform.OnBackPressed
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentSpaceCreateGenericEpoxyFormBinding
import com.imzqqq.app.features.settings.VectorSettingsActivity
import javax.inject.Inject

class CreateSpaceAdd3pidInvitesFragment @Inject constructor(
        private val epoxyController: SpaceAdd3pidEpoxyController
) : VectorBaseFragment<FragmentSpaceCreateGenericEpoxyFormBinding>(),
        SpaceAdd3pidEpoxyController.Listener,
        OnBackPressed {

    private val sharedViewModel: CreateSpaceViewModel by activityViewModel()

    override fun onBackPressed(toolbarButton: Boolean): Boolean {
        sharedViewModel.handle(CreateSpaceAction.OnBackPressed)
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.recyclerView.configureWith(epoxyController)
        epoxyController.listener = this

        sharedViewModel.onEach {
            invalidateState(it)
        }

        views.nextButton.setText(R.string.next_pf)
        views.nextButton.debouncedClicks {
            view.hideKeyboard()
            sharedViewModel.handle(CreateSpaceAction.NextFromAdd3pid)
        }
    }

    private fun invalidateState(it: CreateSpaceState) {
        epoxyController.setData(it)
        val noEmails = it.default3pidInvite?.all { it.value.isNullOrBlank() } ?: true
        views.nextButton.text = if (noEmails) {
            getString(R.string.skip_for_now)
        } else {
            getString(R.string.next_pf)
        }
    }

    override fun onDestroyView() {
        views.recyclerView.cleanup()
        epoxyController.listener = null
        super.onDestroyView()
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            FragmentSpaceCreateGenericEpoxyFormBinding.inflate(layoutInflater, container, false)

    override fun on3pidChange(index: Int, newName: String) {
        sharedViewModel.handle(CreateSpaceAction.DefaultInvite3pidChanged(index, newName))
    }

    override fun onNoIdentityServer() {
        navigator.openSettings(
                requireContext(),
                VectorSettingsActivity.EXTRA_DIRECT_ACCESS_DISCOVERY_SETTINGS
        )
    }
}
