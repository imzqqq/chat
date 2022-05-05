package com.imzqqq.app.features.settings.ignored

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import com.imzqqq.app.core.di.MavericksAssistedViewModelFactory
import com.imzqqq.app.core.di.hiltMavericksViewModelFactory
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.core.platform.VectorViewModelAction
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.user.model.User
import org.matrix.android.sdk.flow.flow

data class IgnoredUsersViewState(
        val ignoredUsers: List<User> = emptyList(),
        val unIgnoreRequest: Async<Unit> = Uninitialized
) : MavericksState

sealed class IgnoredUsersAction : VectorViewModelAction {
    data class UnIgnore(val userId: String) : IgnoredUsersAction()
}

class IgnoredUsersViewModel @AssistedInject constructor(@Assisted initialState: IgnoredUsersViewState,
                                                        private val session: Session) :
    VectorViewModel<IgnoredUsersViewState, IgnoredUsersAction, IgnoredUsersViewEvents>(initialState) {

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<IgnoredUsersViewModel, IgnoredUsersViewState> {
        override fun create(initialState: IgnoredUsersViewState): IgnoredUsersViewModel
    }

    companion object : MavericksViewModelFactory<IgnoredUsersViewModel, IgnoredUsersViewState> by hiltMavericksViewModelFactory()

    init {
        observeIgnoredUsers()
    }

    private fun observeIgnoredUsers() {
        session.flow()
                .liveIgnoredUsers()
                .execute { async ->
                    copy(
                            ignoredUsers = async.invoke().orEmpty()
                    )
                }
    }

    override fun handle(action: IgnoredUsersAction) {
        when (action) {
            is IgnoredUsersAction.UnIgnore -> handleUnIgnore(action)
        }
    }

    private fun handleUnIgnore(action: IgnoredUsersAction.UnIgnore) {
        setState {
            copy(
                    unIgnoreRequest = Loading()
            )
        }

        viewModelScope.launch {
            val result = runCatching { session.unIgnoreUserIds(listOf(action.userId)) }
            setState {
                copy(
                        unIgnoreRequest = result.fold(::Success, ::Fail)
                )
            }
            result.onFailure { _viewEvents.post(IgnoredUsersViewEvents.Failure(it)) }
        }
    }
}
