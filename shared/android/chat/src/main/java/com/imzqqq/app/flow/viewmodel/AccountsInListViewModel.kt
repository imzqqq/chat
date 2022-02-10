package com.imzqqq.app.flow.viewmodel

import com.imzqqq.app.flow.entity.Account
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.util.Either
import com.imzqqq.app.flow.util.Either.Left
import com.imzqqq.app.flow.util.Either.Right
import com.imzqqq.app.flow.util.RxAwareViewModel
import com.imzqqq.app.flow.util.withoutFirstWhich
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

data class State(val accounts: Either<Throwable, List<Account>>, val searchResult: List<Account>?)

class AccountsInListViewModel @Inject constructor(private val api: MastodonApi) : RxAwareViewModel() {

    val state: Observable<State> get() = _state
    private val _state = BehaviorSubject.createDefault(State(Right(listOf()), null))

    fun load(listId: String) {
        val state = _state.value!!
        if (state.accounts.isLeft() || state.accounts.asRight().isEmpty()) {
            api.getAccountsInList(listId, 0).subscribe(
                { accounts ->
                    updateState { copy(accounts = Right(accounts)) }
                },
                { e ->
                    updateState { copy(accounts = Left(e)) }
                }
            ).autoDispose()
        }
    }

    fun addAccountToList(listId: String, account: Account) {
        api.addCountToList(listId, listOf(account.id))
            .subscribe(
                {
                    updateState {
                        copy(accounts = accounts.map { it + account })
                    }
                },
                {
                    Timber.i("Failed to add account to the list: " + account.username)
                }
            )
            .autoDispose()
    }

    fun deleteAccountFromList(listId: String, accountId: String) {
        api.deleteAccountFromList(listId, listOf(accountId))
            .subscribe(
                {
                    updateState {
                        copy(
                            accounts = accounts.map { accounts ->
                                accounts.withoutFirstWhich { it.id == accountId }
                            }
                        )
                    }
                },
                {
                    Timber.i("Failed to remove account from the list: $accountId")
                }
            )
            .autoDispose()
    }

    fun search(query: String) {
        when {
            query.isEmpty() -> updateState { copy(searchResult = null) }
            query.isBlank() -> updateState { copy(searchResult = listOf()) }
            else -> api.searchAccounts(query, null, 10, true)
                .subscribe(
                    { result ->
                        updateState { copy(searchResult = result) }
                    },
                    {
                        updateState { copy(searchResult = listOf()) }
                    }
                ).autoDispose()
        }
    }

    private inline fun updateState(crossinline fn: State.() -> State) {
        _state.onNext(fn(_state.value!!))
    }
}
