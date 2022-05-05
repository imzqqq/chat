package com.imzqqq.app.features.widgets.webview

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.imzqqq.app.R
import com.imzqqq.app.features.themes.ThemeUtils
import com.imzqqq.app.features.webview.VectorWebViewClient
import com.imzqqq.app.features.webview.WebViewEventListener

@SuppressLint("NewApi")
fun WebView.setupForWidget(webViewEventListener: WebViewEventListener) {
    // xml value seems ignored
    setBackgroundColor(ThemeUtils.getColor(context, R.attr.colorSurface))

    // clear caches
    clearHistory()
    clearFormData()

    // Enable Javascript
    settings.javaScriptEnabled = true

    // Use WideViewport and Zoom out if there is no viewport defined
    settings.useWideViewPort = true
    settings.loadWithOverviewMode = true

    // Enable pinch to zoom without the zoom buttons
    settings.builtInZoomControls = true

    // Allow use of Local Storage
    settings.domStorageEnabled = true

    @Suppress("DEPRECATION")
    settings.allowFileAccessFromFileURLs = true
    @Suppress("DEPRECATION")
    settings.allowUniversalAccessFromFileURLs = true

    settings.displayZoomControls = false

    // Permission requests
    webChromeClient = object : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest) {
            WebviewPermissionUtils.promptForPermissions(R.string.room_widget_resource_permission_title, request, context)
        }
    }
    webViewClient = VectorWebViewClient(webViewEventListener)

    val cookieManager = CookieManager.getInstance()
    cookieManager.setAcceptThirdPartyCookies(this, false)
}

fun WebView.clearAfterWidget() {
    // Make sure you remove the WebView from its parent view before doing anything.
    (parent as? ViewGroup)?.removeAllViews()
    webChromeClient = null
    clearHistory()
    // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
    loadUrl("about:blank")
    removeAllViews()
    destroy()
}
