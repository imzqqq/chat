package com.imzqqq.app.flow.components.scheduled

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imzqqq.app.flow.entity.ScheduledStatus
import com.imzqqq.app.flow.network.MastodonApi
import kotlinx.coroutines.rx3.await
import timber.log.Timber

class ScheduledTootPagingSourceFactory(
    private val mastodonApi: MastodonApi
) : () -> ScheduledTootPagingSource {

    private val scheduledTootsCache = mutableListOf<ScheduledStatus>()

    private var pagingSource: ScheduledTootPagingSource? = null

    override fun invoke(): ScheduledTootPagingSource {
        return ScheduledTootPagingSource(mastodonApi, scheduledTootsCache).also {
            pagingSource = it
        }
    }

    fun remove(status: ScheduledStatus) {
        scheduledTootsCache.remove(status)
        pagingSource?.invalidate()
    }
}

class ScheduledTootPagingSource(
    private val mastodonApi: MastodonApi,
    private val scheduledTootsCache: MutableList<ScheduledStatus>
) : PagingSource<String, ScheduledStatus>() {

    override fun getRefreshKey(state: PagingState<String, ScheduledStatus>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ScheduledStatus> {
        return if (params is LoadParams.Refresh && scheduledTootsCache.isNotEmpty()) {
            LoadResult.Page(
                data = scheduledTootsCache,
                prevKey = null,
                nextKey = scheduledTootsCache.lastOrNull()?.id
            )
        } else {
            try {
                val result = mastodonApi.scheduledStatuses(
                    maxId = params.key,
                    limit = params.loadSize
                ).await()

                LoadResult.Page(
                    data = result,
                    prevKey = null,
                    nextKey = result.lastOrNull()?.id
                )
            } catch (e: Exception) {
                Timber.tag("ScheduledTootPgngSrc").w(e, "Error loading scheduled statuses")
                LoadResult.Error(e)
            }
        }
    }
}
