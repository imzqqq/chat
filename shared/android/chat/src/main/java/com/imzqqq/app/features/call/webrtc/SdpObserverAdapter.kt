package com.imzqqq.app.features.call.webrtc

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import timber.log.Timber

open class SdpObserverAdapter : SdpObserver {
    override fun onSetFailure(p0: String?) {
        Timber.e("## SdpObserver: onSetFailure $p0")
    }

    override fun onSetSuccess() {
        Timber.v("## SdpObserver: onSetSuccess")
    }

    override fun onCreateSuccess(p0: SessionDescription?) {
        Timber.v("## SdpObserver: onCreateSuccess $p0")
    }

    override fun onCreateFailure(p0: String?) {
        Timber.e("## SdpObserver: onCreateFailure $p0")
    }
}
