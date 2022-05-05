package com.imzqqq.app.features.createdirect

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.mvrx.activityViewModel
import com.google.zxing.Result
import com.google.zxing.ResultMetadataType
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.PERMISSIONS_FOR_TAKING_PHOTO
import com.imzqqq.app.core.utils.checkPermissions
import com.imzqqq.app.core.utils.onPermissionDeniedDialog
import com.imzqqq.app.core.utils.registerForPermissionsResult
import com.imzqqq.app.databinding.FragmentQrCodeScannerBinding
import com.imzqqq.app.features.userdirectory.PendingSelection
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.matrix.android.sdk.api.session.permalinks.PermalinkData
import org.matrix.android.sdk.api.session.permalinks.PermalinkParser
import org.matrix.android.sdk.api.session.user.model.User
import javax.inject.Inject

class CreateDirectRoomByQrCodeFragment @Inject constructor() : VectorBaseFragment<FragmentQrCodeScannerBinding>(), ZXingScannerView.ResultHandler {

    private val viewModel: CreateDirectRoomViewModel by activityViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentQrCodeScannerBinding {
        return FragmentQrCodeScannerBinding.inflate(inflater, container, false)
    }

    private val openCameraActivityResultLauncher = registerForPermissionsResult { allGranted, deniedPermanently ->
        if (allGranted) {
            startCamera()
        } else if (deniedPermanently) {
            activity?.onPermissionDeniedDialog(R.string.denied_permission_camera)
        }
    }

    private fun startCamera() {
        // Start camera on resume
        views.scannerView.startCamera()
    }

    override fun onResume() {
        super.onResume()
        view?.hideKeyboard()
        // Register ourselves as a handler for scan results.
        views.scannerView.setResultHandler(this)
        // Start camera on resume
        if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO, requireActivity(), openCameraActivityResultLauncher)) {
            startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister ourselves as a handler for scan results.
        views.scannerView.setResultHandler(null)
        // Stop camera on pause
        views.scannerView.stopCamera()
    }

    // Copied from https://github.com/markusfisch/BinaryEye/blob/
    // 9d57889b810dcaa1a91d7278fc45c262afba1284/app/src/main/kotlin/de/markusfisch/android/binaryeye/activity/CameraActivity.kt#L434
    private fun getRawBytes(result: Result): ByteArray? {
        val metadata = result.resultMetadata ?: return null
        val segments = metadata[ResultMetadataType.BYTE_SEGMENTS] ?: return null
        var bytes = ByteArray(0)
        @Suppress("UNCHECKED_CAST")
        for (seg in segments as Iterable<ByteArray>) {
            bytes += seg
        }
        // byte segments can never be shorter than the text.
        // Zxing cuts off content prefixes like "WIFI:"
        return if (bytes.size >= result.text.length) bytes else null
    }

    private fun addByQrCode(value: String) {
        val mxid = (PermalinkParser.parse(value) as? PermalinkData.UserLink)?.userId

        if (mxid === null) {
            Toast.makeText(requireContext(), R.string.invalid_qr_code_uri, Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        } else {
            // The following assumes MXIDs are case insensitive
            if (mxid.equals(other = viewModel.session.myUserId, ignoreCase = true)) {
                Toast.makeText(requireContext(), R.string.cannot_dm_self, Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            } else {
                // Try to get user from known users and fall back to creating a User object from MXID
                val qrInvitee = if (viewModel.session.getUser(mxid) != null) viewModel.session.getUser(mxid)!! else User(mxid, null, null)

                viewModel.handle(
                        CreateDirectRoomAction.CreateRoomAndInviteSelectedUsers(setOf(PendingSelection.UserPendingSelection(qrInvitee)))
                )
            }
        }
    }

    override fun handleResult(result: Result?) {
        if (result === null) {
            Toast.makeText(requireContext(), R.string.qr_code_not_scanned, Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        } else {
            val rawBytes = getRawBytes(result)
            val rawBytesStr = rawBytes?.toString(Charsets.ISO_8859_1)
            val value = rawBytesStr ?: result.text
            addByQrCode(value)
        }
    }
}
