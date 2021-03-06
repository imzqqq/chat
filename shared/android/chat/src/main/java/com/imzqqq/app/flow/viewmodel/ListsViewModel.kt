package com.imzqqq.app.flow.viewmodel

import com.imzqqq.app.flow.entity.MastoList
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.util.RxAwareViewModel
import com.imzqqq.app.flow.util.replacedFirstWhich
import com.imzqqq.app.flow.util.withoutFirstWhich
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.IOException
import java.net.ConnectException
import javax.inject.Inject

class ListsViewModel @Inject constructor(private val api: MastodonApi) : RxAwareViewModel() {
    enum class LoadingState {
        INITIAL, LOADING, LOADED, ERROR_NETWORK, ERROR_OTHER
    }

    enum class Event {
        CREATE_ERROR, DELETE_ERROR, RENAME_ERROR
    }

    data class State(val lists: List<MastoList>, val loadingState: LoadingState)

    val state: Observable<State> get() = _state
    val events: Observable<Event> get() = _events
    private val _state = BehaviorSubject.createDefault(State(listOf(), LoadingState.INITIAL))
    private val _events = PublishSubject.create<Event>()

    fun retryLoading() {
        loadIfNeeded()
    }

    private fun loadIfNeeded() {
        val state = _state.value!!
        if (state.loadingState == LoadingState.LOADING || state.lists.isNotEmpty()) return
        updateState {
            copy(loadingState = LoadingState.LOADING)
        }

        api.getLists().subscribe(
            { lists ->
                updateState {
                    copy(
                        lists = lists,
                        loadingState = LoadingState.LOADED
                    )
                }
            },
            { err ->
                updateState {
                    copy(
                        loadingState = if (err is IOException || err is ConnectException)
                            LoadingState.ERROR_NETWORK else LoadingState.ERROR_OTHER
                    )
                }
            }
        ).autoDispose()
    }

    fun createNewList(listName: String) {
        api.createList(listName).subscribe(
            { list ->
                updateState {
                    copy(lists = lists + list)
                }
            },
            {
                sendEvent(Event.CREATE_ERROR)
            }
        ).autoDispose()
    }

    fun renameList(listId: String, listName: String) {
        api.updateList(listId, listName).subscribe(
            { list ->
                updateState {
                    copy(lists = lists.replacedFirstWhich(list) { it.id == listId })
                }
            },
            {
                sendEvent(Event.RENAME_ERROR)
            }
        ).autoDispose()
    }

    fun deleteList(listId: String) {
        api.deleteList(listId).subscribe(
            {
                updateState {
                    copy(lists = lists.withoutFirstWhich { it.id == listId })
                }
            },
            {
                sendEvent(Event.DELETE_ERROR)
            }
        ).autoDispose()
    }

    private inline fun updateState(crossinline fn: State.() -> State) {
        _state.onNext(fn(_state.value!!))
    }

    private fun sendEvent(event: Event) {
        _events.onNext(event)
    }
}
