package com.imzqqq.app.features.home.room.detail.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseBottomSheetDialogFragment
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.databinding.BottomSheetGenericListWithTitleBinding
import com.imzqqq.app.features.home.room.detail.RoomDetailAction
import com.imzqqq.app.features.home.room.detail.RoomDetailViewModel
import com.imzqqq.app.features.home.room.detail.RoomDetailViewState
import com.imzqqq.app.features.navigation.Navigator
import org.matrix.android.sdk.api.session.widgets.model.Widget
import javax.inject.Inject

/**
 * Bottom sheet displaying active widgets in a room
 */
@AndroidEntryPoint
class RoomWidgetsBottomSheet :
        VectorBaseBottomSheetDialogFragment<BottomSheetGenericListWithTitleBinding>(),
        RoomWidgetsController.Listener {

    @Inject lateinit var epoxyController: RoomWidgetsController
    @Inject lateinit var colorProvider: ColorProvider
    @Inject lateinit var navigator: Navigator

    private val roomDetailViewModel: RoomDetailViewModel by parentFragmentViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetGenericListWithTitleBinding {
        return BottomSheetGenericListWithTitleBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.bottomSheetRecyclerView.configureWith(epoxyController, hasFixedSize = false)
        views.bottomSheetTitle.text = getString(R.string.active_widgets_title)
        views.bottomSheetTitle.textSize = 20f
        views.bottomSheetTitle.setTextColor(colorProvider.getColorFromAttribute(R.attr.vctr_content_primary))
        epoxyController.listener = this
        roomDetailViewModel.onAsync(RoomDetailViewState::activeRoomWidgets) {
            epoxyController.setData(it)
        }
    }

    override fun onDestroyView() {
        views.bottomSheetRecyclerView.cleanup()
        epoxyController.listener = null
        super.onDestroyView()
    }

    override fun didSelectWidget(widget: Widget) = withState(roomDetailViewModel) {
        navigator.openRoomWidget(requireContext(), it.roomId, widget)
        dismiss()
    }

    override fun didSelectManageWidgets() {
        roomDetailViewModel.handle(RoomDetailAction.OpenIntegrationManager)
        dismiss()
    }

    companion object {
        fun newInstance(): RoomWidgetsBottomSheet {
            return RoomWidgetsBottomSheet()
        }
    }
}
