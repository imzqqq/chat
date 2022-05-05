package com.imzqqq.app.features.widgets

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class WidgetAction : VectorViewModelAction {
    data class OnWebViewStartedToLoad(val url: String) : WidgetAction()
    data class OnWebViewLoadingError(val url: String, val isHttpError: Boolean, val errorCode: Int, val errorDescription: String) : WidgetAction()
    data class OnWebViewLoadingSuccess(val url: String) : WidgetAction()
    object LoadFormattedUrl : WidgetAction()
    object DeleteWidget : WidgetAction()
    object RevokeWidget : WidgetAction()
    object OnTermsReviewed : WidgetAction()
}
