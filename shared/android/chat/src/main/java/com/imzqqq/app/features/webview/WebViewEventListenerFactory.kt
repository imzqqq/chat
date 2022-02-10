package com.imzqqq.app.features.webview

import com.imzqqq.app.core.platform.VectorBaseActivity
import org.matrix.android.sdk.api.session.Session

interface WebViewEventListenerFactory {

    /**
     * @return an instance of WebViewEventListener
     */
    fun eventListener(activity: VectorBaseActivity<*>, session: Session): WebViewEventListener
}
