package com.imzqqq.app.features.usercode

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class UserCodeShareViewEvents : VectorViewEvents {
    object Dismiss : UserCodeShareViewEvents()
    object ShowWaitingScreen : UserCodeShareViewEvents()
    object HideWaitingScreen : UserCodeShareViewEvents()
    data class ToastMessage(val message: String) : UserCodeShareViewEvents()
    data class NavigateToRoom(val roomId: String) : UserCodeShareViewEvents()
    data class CameraPermissionNotGranted(val deniedPermanently: Boolean) : UserCodeShareViewEvents()
    data class SharePlainText(val text: String, val title: String, val richPlainText: String) : UserCodeShareViewEvents()
}
