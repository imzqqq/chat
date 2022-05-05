package com.imzqqq.app.features.home.room.detail.timeline.edithistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseBottomSheetDialogFragment
import com.imzqqq.app.databinding.BottomSheetGenericListWithTitleBinding
import com.imzqqq.app.features.home.room.detail.timeline.action.TimelineEventFragmentArgs
import com.imzqqq.app.features.home.room.detail.timeline.item.MessageInformationData
import javax.inject.Inject

/**
 * Bottom sheet displaying list of edits for a given event ordered by timestamp
 */
@AndroidEntryPoint
class ViewEditHistoryBottomSheet :
        VectorBaseBottomSheetDialogFragment<BottomSheetGenericListWithTitleBinding>() {

    private val viewModel: ViewEditHistoryViewModel by fragmentViewModel(ViewEditHistoryViewModel::class)

    @Inject lateinit var epoxyController: ViewEditHistoryEpoxyController

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetGenericListWithTitleBinding {
        return BottomSheetGenericListWithTitleBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.bottomSheetRecyclerView.configureWith(
                epoxyController,
                dividerDrawable = R.drawable.divider_horizontal_on_secondary,
                hasFixedSize = false)
        views.bottomSheetTitle.text = context?.getString(R.string.message_edits)
    }

    override fun onDestroyView() {
        views.bottomSheetRecyclerView.cleanup()
        super.onDestroyView()
    }

    override fun invalidate() = withState(viewModel) {
        epoxyController.setData(it)
        super.invalidate()
    }

    companion object {
        fun newInstance(roomId: String, informationData: MessageInformationData): ViewEditHistoryBottomSheet {
            val args = Bundle()
            val parcelableArgs = TimelineEventFragmentArgs(
                    informationData.eventId,
                    roomId,
                    informationData
            )
            args.putParcelable(Mavericks.KEY_ARG, parcelableArgs)
            return ViewEditHistoryBottomSheet().apply { arguments = args }
        }
    }
}
