package com.imzqqq.app.features.login2.created

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import org.matrix.android.sdk.api.util.MatrixItem

data class AccountCreatedViewState(
        val userId: String = "",
        val isLoading: Boolean = false,
        val currentUser: Async<MatrixItem.UserItem> = Uninitialized,
        val hasBeenModified: Boolean = false
) : MavericksState
