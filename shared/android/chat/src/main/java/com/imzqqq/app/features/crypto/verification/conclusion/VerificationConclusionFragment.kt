package com.imzqqq.app.features.crypto.verification.conclusion

import android.os.Bundle
import android.os.Parcelable
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
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class VerificationConclusionFragment @Inject constructor(
        val controller: VerificationConclusionController
) : VectorBaseFragment<BottomSheetVerificationChildFragmentBinding>(),
        VerificationConclusionController.Listener {

    @Parcelize
    data class Args(
            val isSuccessFull: Boolean,
            val cancelReason: String?,
            val isMe: Boolean
    ) : Parcelable

    private val sharedViewModel by parentFragmentViewModel(VerificationBottomSheetViewModel::class)

    private val viewModel by fragmentViewModel(VerificationConclusionViewModel::class)

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

    override fun onButtonTapped() {
        sharedViewModel.handle(VerificationAction.GotItConclusion)
    }
}
