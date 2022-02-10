/* Copyright 2017 Andrew Dawson
 *
 * This file is a part of Flow.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Flow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Flow; if not,
 * see <http://www.gnu.org/licenses>.
 */

package com.keylesspalace.flow.viewmodel

import android.util.Log
import com.keylesspalace.flow.entity.Account
import com.keylesspalace.flow.network.FlowApi
import com.keylesspalace.flow.util.Either
import com.keylesspalace.flow.util.Either.Left
import com.keylesspalace.flow.util.Either.Right
import com.keylesspalace.flow.util.RxAwareViewModel
import com.keylesspalace.flow.util.withoutFirstWhich
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

data class State(val accounts: Either<Throwable, List<Account>>, val searchResult: List<Account>?)

class AccountsInListViewModel @Inject constructor(private val api: FlowApi) : RxAwareViewModel() {

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
                    Log.i(
                        javaClass.simpleName,
                        "Failed to add account to the list: ${account.username}"
                    )
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
                    Log.i(javaClass.simpleName, "Failed to remove account from thelist: $accountId")
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
