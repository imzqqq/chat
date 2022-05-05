package com.imzqqq.app.features.userdirectory

import com.imzqqq.app.features.displayname.getBestName
import org.matrix.android.sdk.api.session.identity.ThreePid
import org.matrix.android.sdk.api.session.user.model.User
import org.matrix.android.sdk.api.util.toMatrixItem

sealed class PendingSelection {
    data class UserPendingSelection(val user: User) : PendingSelection()
    data class ThreePidPendingSelection(val threePid: ThreePid) : PendingSelection()

    fun getBestName(): String {
        return when (this) {
            is UserPendingSelection     -> user.toMatrixItem().getBestName()
            is ThreePidPendingSelection -> threePid.value
        }
    }

    fun getMxId(): String {
        return when (this) {
            is UserPendingSelection     -> user.userId
            is ThreePidPendingSelection -> threePid.value
        }
    }
}
