package com.imzqqq.app.features.webview

import com.imzqqq.app.core.platform.VectorBaseActivity
import org.matrix.android.sdk.api.session.Session

/**
 * This enum indicates the WebView mode. It's responsible for creating a WebViewEventListener
 */
enum class WebViewMode : WebViewEventListenerFactory {

    DEFAULT {
        override fun eventListener(activity: VectorBaseActivity<*>, session: Session): WebViewEventListener {
            return DefaultWebViewEventListener()
        }
    },
    CONSENT {
        override fun eventListener(activity: VectorBaseActivity<*>, session: Session): WebViewEventListener {
            return ConsentWebViewEventListener(activity, session, DefaultWebViewEventListener())
        }
    };
}
