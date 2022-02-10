/* Copyright 2019 Joel Pyska
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
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.flow.components.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.keylesspalace.flow.appstore.BlockEvent
import com.keylesspalace.flow.appstore.EventHub
import com.keylesspalace.flow.appstore.MuteEvent
import com.keylesspalace.flow.components.report.adapter.StatusesPagingSource
import com.keylesspalace.flow.components.report.model.StatusViewState
import com.keylesspalace.flow.entity.Relationship
import com.keylesspalace.flow.entity.Status
import com.keylesspalace.flow.network.FlowApi
import com.keylesspalace.flow.util.Error
import com.keylesspalace.flow.util.Loading
import com.keylesspalace.flow.util.Resource
import com.keylesspalace.flow.util.RxAwareViewModel
import com.keylesspalace.flow.util.Success
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReportViewModel @Inject constructor(
    private val flowApi: FlowApi,
    private val eventHub: EventHub
) : RxAwareViewModel() {

    private val navigationMutable = MutableLiveData<Screen?>()
    val navigation: LiveData<Screen?> = navigationMutable

    private val muteStateMutable = MutableLiveData<Resource<Boolean>>()
    val muteState: LiveData<Resource<Boolean>> = muteStateMutable

    private val blockStateMutable = MutableLiveData<Resource<Boolean>>()
    val blockState: LiveData<Resource<Boolean>> = blockStateMutable

    private val reportingStateMutable = MutableLiveData<Resource<Boolean>>()
    var reportingState: LiveData<Resource<Boolean>> = reportingStateMutable

    private val checkUrlMutable = MutableLiveData<String?>()
    val checkUrl: LiveData<String?> = checkUrlMutable

    private val accountIdFlow = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val statusesFlow = accountIdFlow.flatMapLatest { accountId ->
        Pager(
            initialKey = statusId,
            config = PagingConfig(pageSize = 20, initialLoadSize = 20),
            pagingSourceFactory = { StatusesPagingSource(accountId, flowApi) }
        ).flow
    }
        .cachedIn(viewModelScope)

    private val selectedIds = HashSet<String>()
    val statusViewState = StatusViewState()

    var reportNote: String = ""
    var isRemoteNotify = false

    private var statusId: String? = null
    lateinit var accountUserName: String
    lateinit var accountId: String
    var isRemoteAccount: Boolean = false
    var remoteServer: String? = null

    fun init(accountId: String, userName: String, statusId: String?) {
        this.accountId = accountId
        this.accountUserName = userName
        this.statusId = statusId
        statusId?.let {
            selectedIds.add(it)
        }

        isRemoteAccount = userName.contains('@')
        if (isRemoteAccount) {
            remoteServer = userName.substring(userName.indexOf('@') + 1)
        }

        obtainRelationship()

        viewModelScope.launch {
            accountIdFlow.emit(accountId)
        }
    }

    fun navigateTo(screen: Screen) {
        navigationMutable.value = screen
    }

    fun navigated() {
        navigationMutable.value = null
    }

    private fun obtainRelationship() {
        val ids = listOf(accountId)
        muteStateMutable.value = Loading()
        blockStateMutable.value = Loading()
        flowApi.relationships(ids)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    updateRelationship(data.getOrNull(0))
                },
                {
                    updateRelationship(null)
                }
            )
            .autoDispose()
    }

    private fun updateRelationship(relationship: Relationship?) {
        if (relationship != null) {
            muteStateMutable.value = Success(relationship.muting)
            blockStateMutable.value = Success(relationship.blocking)
        } else {
            muteStateMutable.value = Error(false)
            blockStateMutable.value = Error(false)
        }
    }

    fun toggleMute() {
        val alreadyMuted = muteStateMutable.value?.data == true
        if (alreadyMuted) {
            flowApi.unmuteAccount(accountId)
        } else {
            flowApi.muteAccount(accountId)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { relationship ->
                    val muting = relationship?.muting == true
                    muteStateMutable.value = Success(muting)
                    if (muting) {
                        eventHub.dispatch(MuteEvent(accountId))
                    }
                },
                { error ->
                    muteStateMutable.value = Error(false, error.message)
                }
            ).autoDispose()

        muteStateMutable.value = Loading()
    }

    fun toggleBlock() {
        val alreadyBlocked = blockStateMutable.value?.data == true
        if (alreadyBlocked) {
            flowApi.unblockAccount(accountId)
        } else {
            flowApi.blockAccount(accountId)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { relationship ->
                    val blocking = relationship?.blocking == true
                    blockStateMutable.value = Success(blocking)
                    if (blocking) {
                        eventHub.dispatch(BlockEvent(accountId))
                    }
                },
                { error ->
                    blockStateMutable.value = Error(false, error.message)
                }
            )
            .autoDispose()

        blockStateMutable.value = Loading()
    }

    fun doReport() {
        reportingStateMutable.value = Loading()
        flowApi.reportObservable(accountId, selectedIds.toList(), reportNote, if (isRemoteAccount) isRemoteNotify else null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    reportingStateMutable.value = Success(true)
                },
                { error ->
                    reportingStateMutable.value = Error(cause = error)
                }
            )
            .autoDispose()
    }

    fun checkClickedUrl(url: String?) {
        checkUrlMutable.value = url
    }

    fun urlChecked() {
        checkUrlMutable.value = null
    }

    fun setStatusChecked(status: Status, checked: Boolean) {
        if (checked) {
            selectedIds.add(status.id)
        } else {
            selectedIds.remove(status.id)
        }
    }

    fun isStatusChecked(id: String): Boolean {
        return selectedIds.contains(id)
    }
}
