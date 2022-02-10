package com.imzqqq.app.features.home

import com.imzqqq.app.core.platform.VectorViewEvents
import org.matrix.android.sdk.api.util.MatrixItem

sealed class HomeActivityViewEvents : VectorViewEvents {
    data class AskPasswordToInitCrossSigning(val userItem: MatrixItem.UserItem?) : HomeActivityViewEvents()
    data class OnNewSession(val userItem: MatrixItem.UserItem?, val waitForIncomingRequest: Boolean = true) : HomeActivityViewEvents()
    data class OnCrossSignedInvalidated(val userItem: MatrixItem.UserItem) : HomeActivityViewEvents()
    object PromptToEnableSessionPush : HomeActivityViewEvents()
}
