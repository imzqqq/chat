package com.imzqqq.app.features.settings.devtools

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.accountdata.UserAccountDataEvent
import org.matrix.android.sdk.flow.flow

data class AccountDataViewState(
        val accountData: Async<List<UserAccountDataEvent>> = Uninitialized
) : MavericksState

class AccountDataViewModel @AssistedInject constructor(@Assisted initialState: AccountDataViewState,
                                                       private val session: Session) :
    VectorViewModel<AccountDataViewState, AccountDataAction, EmptyViewEvents>(initialState) {

    init {
        session.flow().liveUserAccountData(emptySet())
                .execute {
                    copy(accountData = it)
                }
    }

    override fun handle(action: AccountDataAction) {
        when (action) {
            is AccountDataAction.DeleteAccountData -> handleDeleteAccountData(action)
        }.exhaustive
    }

    private fun handleDeleteAccountData(action: AccountDataAction.DeleteAccountData) {
        viewModelScope.launch {
            session.accountDataService().updateUserAccountData(action.type, emptyMap())
        }
    }

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<AccountDataViewModel, AccountDataViewState> {
        override fun create(initialState: AccountDataViewState): AccountDataViewModel
    }

    companion object : MavericksViewModelFactory<AccountDataViewModel, AccountDataViewState> by hiltMavericksViewModelFactory()
}
