package com.imzqqq.app.features.crypto.verification.qrconfirmation

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

class VerificationQrScannedByOtherFragment @Inject constructor(
        val controller: VerificationQrScannedByOtherController
) : VectorBaseFragment<BottomSheetVerificationChildFragmentBinding>(),
        VerificationQrScannedByOtherController.Listener {

    private val sharedViewModel by parentFragmentViewModel(VerificationBottomSheetViewModel::class)

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetVerificationChildFragmentBinding {
        return BottomSheetVerificationChildFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun invalidate() = withState(sharedViewModel) { state ->
        controller.update(state)
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

    override fun onUserConfirmsQrCodeScanned() {
        sharedViewModel.handle(VerificationAction.OtherUserScannedSuccessfully)
    }

    override fun onUserDeniesQrCodeScanned() {
        sharedViewModel.handle(VerificationAction.OtherUserDidNotScanned)
    }
}
