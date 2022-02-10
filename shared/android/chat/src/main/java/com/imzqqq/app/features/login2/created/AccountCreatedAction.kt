package com.imzqqq.app.features.login2.created

import android.net.Uri
import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class AccountCreatedAction : VectorViewModelAction {
    data class SetDisplayName(val displayName: String) : AccountCreatedAction()
    data class SetAvatar(val avatarUri: Uri, val filename: String) : AccountCreatedAction()
}
