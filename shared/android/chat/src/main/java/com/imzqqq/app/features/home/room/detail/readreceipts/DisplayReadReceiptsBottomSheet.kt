package com.imzqqq.app.features.home.room.detail.readreceipts

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.args
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseBottomSheetDialogFragment
import com.imzqqq.app.databinding.BottomSheetGenericListWithTitleBinding
import com.imzqqq.app.features.home.room.detail.timeline.action.EventSharedAction
import com.imzqqq.app.features.home.room.detail.timeline.action.MessageSharedActionViewModel
import com.imzqqq.app.features.home.room.detail.timeline.item.ReadReceiptData
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class DisplayReadReceiptArgs(
        val readReceipts: List<ReadReceiptData>
) : Parcelable

/**
 * Bottom sheet displaying list of read receipts for a given event ordered by descending timestamp
 */
@AndroidEntryPoint
class DisplayReadReceiptsBottomSheet :
        VectorBaseBottomSheetDialogFragment<BottomSheetGenericListWithTitleBinding>(),
        DisplayReadReceiptsController.Listener {

    @Inject lateinit var epoxyController: DisplayReadReceiptsController

    private val displayReadReceiptArgs: DisplayReadReceiptArgs by args()

    private lateinit var sharedActionViewModel: MessageSharedActionViewModel

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetGenericListWithTitleBinding {
        return BottomSheetGenericListWithTitleBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedActionViewModel = activityViewModelProvider.get(MessageSharedActionViewModel::class.java)
        views.bottomSheetRecyclerView.configureWith(epoxyController, hasFixedSize = false)
        views.bottomSheetTitle.text = getString(R.string.seen_by)
        epoxyController.listener = this
        epoxyController.setData(displayReadReceiptArgs.readReceipts)
    }

    override fun onDestroyView() {
        views.bottomSheetRecyclerView.cleanup()
        epoxyController.listener = null
        super.onDestroyView()
    }

    override fun didSelectUser(userId: String) {
        sharedActionViewModel.post(EventSharedAction.OpenUserProfile(userId))
    }

    // we are not using state for this one as it's static, so no need to override invalidate()

    companion object {
        fun newInstance(readReceipts: List<ReadReceiptData>): DisplayReadReceiptsBottomSheet {
            val args = Bundle()
            val parcelableArgs = DisplayReadReceiptArgs(
                    readReceipts
            )
            args.putParcelable(Mavericks.KEY_ARG, parcelableArgs)
            return DisplayReadReceiptsBottomSheet().apply { arguments = args }
        }
    }
}
