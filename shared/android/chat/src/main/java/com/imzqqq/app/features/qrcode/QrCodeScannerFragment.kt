package com.imzqqq.app.features.qrcode

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.zxing.Result
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentQrCodeScannerBinding
import me.dm7.barcodescanner.zxing.ZXingScannerView
import javax.inject.Inject

class QrCodeScannerFragment @Inject constructor() :
    VectorBaseFragment<FragmentQrCodeScannerBinding>(),
        ZXingScannerView.ResultHandler {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentQrCodeScannerBinding {
        return FragmentQrCodeScannerBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        // Register ourselves as a handler for scan results.
        views.scannerView.setResultHandler(this)
        // Start camera on resume
        views.scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        // Stop camera on pause
        views.scannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        // Do something with the result here
        // This is not intended to be used outside of QrCodeScannerActivity for the moment
        (requireActivity() as? QrCodeScannerActivity)?.setResultAndFinish(rawResult)
    }
}
