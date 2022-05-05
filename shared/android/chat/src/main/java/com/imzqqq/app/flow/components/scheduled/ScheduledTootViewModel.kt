package com.imzqqq.app.flow.components.scheduled

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.imzqqq.app.flow.appstore.EventHub
import com.imzqqq.app.flow.entity.ScheduledStatus
import com.imzqqq.app.flow.network.MastodonApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.await
import timber.log.Timber
import javax.inject.Inject

class ScheduledTootViewModel @Inject constructor(
    val mastodonApi: MastodonApi,
    val eventHub: EventHub
) : ViewModel() {

    private val pagingSourceFactory = ScheduledTootPagingSourceFactory(mastodonApi)

    val data = Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 20),
        pagingSourceFactory = pagingSourceFactory
    ).flow
        .cachedIn(viewModelScope)

    fun deleteScheduledStatus(status: ScheduledStatus) {
        viewModelScope.launch {
            try {
                mastodonApi.deleteScheduledStatus(status.id).await()
                pagingSourceFactory.remove(status)
            } catch (throwable: Throwable) {
                Timber.tag("ScheduledTootViewModel").w(throwable, "Error deleting scheduled status")
            }
        }
    }
}
