package com.imzqqq.app.features.crypto.verification.choose

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.registerStartForActivityResult
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.PERMISSIONS_FOR_TAKING_PHOTO
import com.imzqqq.app.core.utils.checkPermissions
import com.imzqqq.app.core.utils.onPermissionDeniedDialog
import com.imzqqq.app.core.utils.registerForPermissionsResult
import com.imzqqq.app.databinding.BottomSheetVerificationChildFragmentBinding
import com.imzqqq.app.features.crypto.verification.VerificationAction
import com.imzqqq.app.features.crypto.verification.VerificationBottomSheetViewModel
import com.imzqqq.app.features.qrcode.QrCodeScannerActivity
import timber.log.Timber
import javax.inject.Inject

class VerificationChooseMethodFragment @Inject constructor(
        val controller: VerificationChooseMethodController
) : VectorBaseFragment<BottomSheetVerificationChildFragmentBinding>(),
        VerificationChooseMethodController.Listener {

    private val viewModel by fragmentViewModel(VerificationChooseMethodViewModel::class)

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

    override fun doVerifyBySas() = withState(sharedViewModel) { state ->
        sharedViewModel.handle(VerificationAction.StartSASVerification(
                state.otherUserMxItem?.id ?: "",
                state.pendingRequest.invoke()?.transactionId ?: ""))
    }

    private val openCameraActivityResultLauncher = registerForPermissionsResult { allGranted, deniedPermanently ->
        if (allGranted) {
            doOpenQRCodeScanner()
        } else if (deniedPermanently) {
            activity?.onPermissionDeniedDialog(R.string.denied_permission_camera)
        }
    }

    override fun openCamera() {
        if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO, requireActivity(), openCameraActivityResultLauncher)) {
            doOpenQRCodeScanner()
        }
    }

    override fun onClickOnWasNotMe() {
        sharedViewModel.itWasNotMe()
    }

    private fun doOpenQRCodeScanner() {
        QrCodeScannerActivity.startForResult(requireActivity(), scanActivityResultLauncher)
    }

    private val scanActivityResultLauncher = registerStartForActivityResult { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val scannedQrCode = QrCodeScannerActivity.getResultText(activityResult.data)
            val wasQrCode = QrCodeScannerActivity.getResultIsQrCode(activityResult.data)

            if (wasQrCode && !scannedQrCode.isNullOrBlank()) {
                onRemoteQrCodeScanned(scannedQrCode)
            } else {
                Timber.w("It was not a QR code, or empty result")
            }
        }
    }

    private fun onRemoteQrCodeScanned(remoteQrCode: String) = withState(sharedViewModel) { state ->
        sharedViewModel.handle(VerificationAction.RemoteQrCodeScanned(
                state.otherUserMxItem?.id ?: "",
                state.pendingRequest.invoke()?.transactionId ?: "",
                remoteQrCode
        ))
    }
}
