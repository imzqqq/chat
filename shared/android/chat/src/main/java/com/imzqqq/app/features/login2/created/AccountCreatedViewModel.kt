package com.imzqqq.app.features.login2.created

import com.airbnb.mvrx.MavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.VectorViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.MatrixPatterns
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.util.MatrixItem
import org.matrix.android.sdk.api.util.toMatrixItem
import org.matrix.android.sdk.flow.flow
import org.matrix.android.sdk.flow.unwrap
import timber.log.Timber

class AccountCreatedViewModel @AssistedInject constructor(
        @Assisted initialState: AccountCreatedViewState,
        private val session: Session
) : VectorViewModel<AccountCreatedViewState, AccountCreatedAction, AccountCreatedViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<AccountCreatedViewModel, AccountCreatedViewState> {
        override fun create(initialState: AccountCreatedViewState): AccountCreatedViewModel
    }

    companion object : MavericksViewModelFactory<AccountCreatedViewModel, AccountCreatedViewState> by hiltMavericksViewModelFactory()

    init {
        setState {
            copy(
                    userId = session.myUserId
            )
        }
        observeUser()
    }

    private fun observeUser() {
        session.flow()
                .liveUser(session.myUserId)
                .unwrap()
                .map {
                    if (MatrixPatterns.isUserId(it.userId)) {
                        it.toMatrixItem()
                    } else {
                        Timber.w("liveUser() has returned an invalid user: $it")
                        MatrixItem.UserItem(session.myUserId, null, null)
                    }
                }
                .execute {
                    copy(currentUser = it)
                }
    }

    override fun handle(action: AccountCreatedAction) {
        when (action) {
            is AccountCreatedAction.SetAvatar      -> handleSetAvatar(action)
            is AccountCreatedAction.SetDisplayName -> handleSetDisplayName(action)
        }
    }

    private fun handleSetAvatar(action: AccountCreatedAction.SetAvatar) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            val result = runCatching { session.updateAvatar(session.myUserId, action.avatarUri, action.filename) }
                    .onFailure { _viewEvents.post(AccountCreatedViewEvents.Failure(it)) }
            setState {
                copy(
                        isLoading = false,
                        hasBeenModified = hasBeenModified || result.isSuccess
                )
            }
        }
    }

    private fun handleSetDisplayName(action: AccountCreatedAction.SetDisplayName) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            val result = runCatching { session.setDisplayName(session.myUserId, action.displayName) }
                    .onFailure { _viewEvents.post(AccountCreatedViewEvents.Failure(it)) }
            setState {
                copy(
                        isLoading = false,
                        hasBeenModified = hasBeenModified || result.isSuccess
                )
            }
        }
    }
}
