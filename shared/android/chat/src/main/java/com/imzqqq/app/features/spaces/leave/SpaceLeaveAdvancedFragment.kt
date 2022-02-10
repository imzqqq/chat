package com.imzqqq.app.features.spaces.leave

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentSpaceLeaveAdvancedBinding
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import reactivecircus.flowbinding.appcompat.queryTextChanges
import javax.inject.Inject

class SpaceLeaveAdvancedFragment @Inject constructor(
        val controller: SelectChildrenController
) : VectorBaseFragment<FragmentSpaceLeaveAdvancedBinding>(),
        SelectChildrenController.Listener {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            FragmentSpaceLeaveAdvancedBinding.inflate(layoutInflater, container, false)

    val viewModel: SpaceLeaveAdvancedViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(views.toolbar)
        controller.listener = this
        views.roomList.configureWith(controller)
        views.spaceLeaveCancel.debouncedClicks { requireActivity().finish() }

        views.spaceLeaveButton.debouncedClicks {
            viewModel.handle(SpaceLeaveAdvanceViewAction.DoLeave)
        }

        views.publicRoomsFilter.queryTextChanges()
                .debounce(100)
                .onEach {
                    viewModel.handle(SpaceLeaveAdvanceViewAction.UpdateFilter(it.toString()))
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        controller.listener = null
        views.roomList.cleanup()
        super.onDestroyView()
    }

    override fun invalidate() = withState(viewModel) { state ->
        super.invalidate()
        controller.setData(state)
    }

    override fun onItemSelected(roomSummary: RoomSummary) {
        viewModel.handle(SpaceLeaveAdvanceViewAction.ToggleSelection(roomSummary.roomId))
    }
}
