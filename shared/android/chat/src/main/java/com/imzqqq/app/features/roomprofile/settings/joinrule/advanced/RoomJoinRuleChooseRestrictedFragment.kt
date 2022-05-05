package com.imzqqq.app.features.roomprofile.settings.joinrule.advanced

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.OnBackPressed
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentSpaceRestrictedSelectBinding
import com.imzqqq.app.features.home.AvatarRenderer
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.matrix.android.sdk.api.util.MatrixItem
import reactivecircus.flowbinding.appcompat.queryTextChanges
import javax.inject.Inject

class RoomJoinRuleChooseRestrictedFragment @Inject constructor(
        val controller: ChooseRestrictedController,
        val avatarRenderer: AvatarRenderer
) : VectorBaseFragment<FragmentSpaceRestrictedSelectBinding>(),
        ChooseRestrictedController.Listener,
        OnBackPressed {

    private val viewModel: RoomJoinRuleChooseRestrictedViewModel by activityViewModel(RoomJoinRuleChooseRestrictedViewModel::class)

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            FragmentSpaceRestrictedSelectBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller.listener = this
        views.recyclerView.configureWith(controller)
        views.roomsFilter.queryTextChanges()
                .debounce(500)
                .onEach {
                    viewModel.handle(RoomJoinRuleChooseRestrictedActions.FilterWith(it.toString()))
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

        views.okButton.debouncedClicks {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        controller.listener = null
        views.recyclerView.cleanup()
        super.onDestroyView()
    }

    override fun invalidate() = withState(viewModel) { state ->
        super.invalidate()
        controller.setData(state)
    }

    override fun onBackPressed(toolbarButton: Boolean): Boolean {
        val filter = views.roomsFilter.query
        if (filter.isEmpty()) {
            parentFragmentManager.popBackStack()
        } else {
            views.roomsFilter.setQuery("", true)
        }
        return true
    }

    override fun onItemSelected(matrixItem: MatrixItem) {
        viewModel.handle(RoomJoinRuleChooseRestrictedActions.ToggleSelection(matrixItem))
    }
}
