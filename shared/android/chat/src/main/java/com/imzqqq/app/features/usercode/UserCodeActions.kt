package com.imzqqq.app.features.usercode

import com.imzqqq.app.core.platform.VectorViewModelAction
import org.matrix.android.sdk.api.util.MatrixItem

sealed class UserCodeActions : VectorViewModelAction {
    object DismissAction : UserCodeActions()
    data class SwitchMode(val mode: UserCodeState.Mode) : UserCodeActions()
    data class DecodedQRCode(val code: String) : UserCodeActions()
    data class StartChattingWithUser(val matrixItem: MatrixItem) : UserCodeActions()
    data class CameraPermissionNotGranted(val deniedPermanently: Boolean) : UserCodeActions()
    object ShareByText : UserCodeActions()
}
