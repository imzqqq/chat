package com.imzqqq.app.features.crypto.verification.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.BottomSheetVerificationChildFragmentBinding
import com.imzqqq.app.features.crypto.verification.VerificationAction
import com.imzqqq.app.features.crypto.verification.VerificationBottomSheetViewModel
import javax.inject.Inject

class VerificationRequestFragment @Inject constructor(
        val controller: VerificationRequestController
) : VectorBaseFragment<BottomSheetVerificationChildFragmentBinding>(),
        VerificationRequestController.Listener {

    private val viewModel by parentFragmentViewModel(VerificationBottomSheetViewModel::class)

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetVerificationChildFragmentBinding {
        return BottomSheetVerificationChildFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onDestroyView() {
        views.bottomSheetVerificationRecyclerView.cleanup()
        controller.listener = null
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        views.bottomSheetVerificationRecyclerView.configureWith(controller, hasFixedSize = false, disableItemAnimation = true)
        controller.listener = this
    }

    override fun invalidate() = withState(viewModel) { state ->
        controller.update(state)
    }

    override fun onClickOnVerificationStart(): Unit = withState(viewModel) { state ->
        state.otherUserMxItem?.id?.let { otherUserId ->
            viewModel.handle(VerificationAction.RequestVerificationByDM(otherUserId, state.roomId))
        }
    }

    override fun onClickRecoverFromPassphrase() {
        viewModel.handle(VerificationAction.VerifyFromPassphrase)
    }

    override fun onClickDismiss() {
        viewModel.handle(VerificationAction.SkipVerification)
    }

    override fun onClickSkip() {
        viewModel.queryCancel()
    }

    override fun onClickOnWasNotMe() {
        viewModel.itWasNotMe()
    }
}
