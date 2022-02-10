package com.imzqqq.app.features.home.room.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.parentFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.platform.ButtonStateView
import com.imzqqq.app.core.platform.VectorBaseBottomSheetDialogFragment
import com.imzqqq.app.databinding.BottomSheetTombstoneJoinBinding
import javax.inject.Inject

@AndroidEntryPoint
class JoinReplacementRoomBottomSheet :
        VectorBaseBottomSheetDialogFragment<BottomSheetTombstoneJoinBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            BottomSheetTombstoneJoinBinding.inflate(inflater, container, false)

    @Inject
    lateinit var errorFormatter: ErrorFormatter

    private val viewModel: RoomDetailViewModel by parentFragmentViewModel()

    override val showExpanded: Boolean
        get() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.roomUpgradeButton.retryClicked = object : ClickListener {
            override fun invoke(view: View) {
                viewModel.handle(RoomDetailAction.JoinAndOpenReplacementRoom)
            }
        }

        viewModel.onEach(RoomDetailViewState::joinUpgradedRoomAsync) { joinState ->
            when (joinState) {
                // it should never be Uninitialized
                Uninitialized,
                is Loading    -> {
                    views.roomUpgradeButton.render(ButtonStateView.State.Loading)
                    views.descriptionText.setText(R.string.it_may_take_some_time)
                }
                is Success    -> {
                    views.roomUpgradeButton.render(ButtonStateView.State.Loaded)
                    dismiss()
                }
                is Fail       -> {
                    // display the error message
                    views.descriptionText.text = errorFormatter.toHumanReadable(joinState.error)
                    views.roomUpgradeButton.render(ButtonStateView.State.Error)
                }
            }
        }
    }
}
