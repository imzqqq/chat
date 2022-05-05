package com.imzqqq.app.features.userdirectory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserListFragmentArgs(
        val title: String,
        val menuResId: Int,
        val excludedUserIds: Set<String>? = null,
        val singleSelection: Boolean = false,
        val showInviteActions: Boolean = true,
        val showContactBookAction: Boolean = true,
        val showToolbar: Boolean = true
) : Parcelable
