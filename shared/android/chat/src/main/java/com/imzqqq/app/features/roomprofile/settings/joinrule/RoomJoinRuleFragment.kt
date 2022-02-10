package com.imzqqq.app.features.roomprofile.settings.joinrule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.OnBackPressed
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentJoinRulesRecyclerBinding
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.roomprofile.settings.joinrule.advanced.RoomJoinRuleChooseRestrictedActions
import com.imzqqq.app.features.roomprofile.settings.joinrule.advanced.RoomJoinRuleChooseRestrictedViewModel
import org.matrix.android.sdk.api.session.room.model.RoomJoinRules
import javax.inject.Inject

class RoomJoinRuleFragment @Inject constructor(
        val controller: RoomJoinRuleAdvancedController,
        val avatarRenderer: AvatarRenderer
) : VectorBaseFragment<FragmentJoinRulesRecyclerBinding>(),
        OnBackPressed, RoomJoinRuleAdvancedController.InteractionListener {

    private val viewModel: RoomJoinRuleChooseRestrictedViewModel by activityViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            FragmentJoinRulesRecyclerBinding.inflate(inflater, container, false)

    override fun onBackPressed(toolbarButton: Boolean): Boolean {
        val hasUnsavedChanges = withState(viewModel) { it.hasUnsavedChanges }
        val isLoading = withState(viewModel) { it.updatingStatus is Loading }
        if (!hasUnsavedChanges || isLoading) {
            requireActivity().finish()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.dialog_title_warning)
                    .setMessage(R.string.warning_unsaved_change)
                    .setPositiveButton(R.string.warning_unsaved_change_discard) { _, _ ->
                        requireActivity().finish()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            return true
        }
        return true
    }

    override fun invalidate() = withState(viewModel) { state ->
        super.invalidate()
        controller.setData(state)
        if (state.hasUnsavedChanges) {
            // show discard and save
            views.cancelButton.isVisible = true
            views.positiveButton.text = getString(R.string.warning_unsaved_change_discard)
            views.positiveButton.isVisible = true
            views.positiveButton.text = getString(R.string.save)
            views.positiveButton.debouncedClicks {
                viewModel.handle(RoomJoinRuleChooseRestrictedActions.DoUpdateJoinRules)
            }
        } else {
            views.cancelButton.isVisible = false
            views.positiveButton.isVisible = true
            views.positiveButton.text = getString(R.string.ok)
            views.positiveButton.debouncedClicks { requireActivity().finish() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.genericRecyclerView.configureWith(controller, hasFixedSize = true)
        controller.interactionListener = this
        views.cancelButton.debouncedClicks { requireActivity().finish() }
    }

    override fun onDestroyView() {
        views.genericRecyclerView.cleanup()
        super.onDestroyView()
    }

    override fun didSelectRule(rules: RoomJoinRules) {
        val isLoading = withState(viewModel) { it.updatingStatus is Loading }
        if (isLoading) return

        viewModel.handle(RoomJoinRuleChooseRestrictedActions.SelectJoinRules(rules))
    }
}
