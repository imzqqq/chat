package com.imzqqq.app.features.crypto.verification.emoji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.BottomSheetVerificationChildFragmentBinding
import com.imzqqq.app.features.crypto.verification.VerificationAction
import com.imzqqq.app.features.crypto.verification.VerificationBottomSheetViewModel
import javax.inject.Inject

class VerificationEmojiCodeFragment @Inject constructor(
        val controller: VerificationEmojiCodeController
) : VectorBaseFragment<BottomSheetVerificationChildFragmentBinding>(),
        VerificationEmojiCodeController.Listener {

    private val viewModel by fragmentViewModel(VerificationEmojiCodeViewModel::class)

    private val sharedViewModel by parentFragmentViewModel(VerificationBottomSheetViewModel::class)

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

    override fun onMatchButtonTapped() = withState(viewModel) { state ->
        val otherUserId = state.otherUser?.id ?: return@withState
        val txId = state.transactionId ?: return@withState
        sharedViewModel.handle(VerificationAction.SASMatchAction(otherUserId, txId))
    }

    override fun onDoNotMatchButtonTapped() = withState(viewModel) { state ->
        val otherUserId = state.otherUser?.id ?: return@withState
        val txId = state.transactionId ?: return@withState
        sharedViewModel.handle(VerificationAction.SASDoNotMatchAction(otherUserId, txId))
    }
}
