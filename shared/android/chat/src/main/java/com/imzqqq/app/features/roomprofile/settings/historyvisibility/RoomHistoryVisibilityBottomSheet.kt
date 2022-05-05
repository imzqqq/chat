package com.imzqqq.app.features.roomprofile.settings.historyvisibility

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.core.ui.bottomsheet.BottomSheetGeneric
import com.imzqqq.app.core.ui.bottomsheet.BottomSheetGenericController
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.session.room.model.RoomHistoryVisibility
import javax.inject.Inject

@Parcelize
data class RoomHistoryVisibilityBottomSheetArgs(
        val currentRoomHistoryVisibility: RoomHistoryVisibility
) : Parcelable

@AndroidEntryPoint
class RoomHistoryVisibilityBottomSheet : BottomSheetGeneric<RoomHistoryVisibilityState, RoomHistoryVisibilityRadioAction>() {

    private lateinit var roomHistoryVisibilitySharedActionViewModel: RoomHistoryVisibilitySharedActionViewModel
    @Inject lateinit var controller: RoomHistoryVisibilityController
    private val viewModel: RoomHistoryVisibilityViewModel by fragmentViewModel(RoomHistoryVisibilityViewModel::class)

    override fun getController(): BottomSheetGenericController<RoomHistoryVisibilityState, RoomHistoryVisibilityRadioAction> = controller

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomHistoryVisibilitySharedActionViewModel = activityViewModelProvider.get(RoomHistoryVisibilitySharedActionViewModel::class.java)
    }

    override fun didSelectAction(action: RoomHistoryVisibilityRadioAction) {
        roomHistoryVisibilitySharedActionViewModel.post(action)
        dismiss()
    }

    override fun invalidate() = withState(viewModel) {
        controller.setData(it)
        super.invalidate()
    }

    companion object {
        fun newInstance(currentRoomHistoryVisibility: RoomHistoryVisibility): RoomHistoryVisibilityBottomSheet {
            return RoomHistoryVisibilityBottomSheet().apply {
                setArguments(RoomHistoryVisibilityBottomSheetArgs(currentRoomHistoryVisibility))
            }
        }
    }
}
