package com.imzqqq.app.flow.components.report.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.network.MastodonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.rx3.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class StatusesPagingSource(
    private val accountId: String,
    private val mastodonApi: MastodonApi
) : PagingSource<String, Status>() {

    override fun getRefreshKey(state: PagingState<String, Status>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Status> {
        val key = params.key
        try {
            val result = if (params is LoadParams.Refresh && key != null) {
                withContext(Dispatchers.IO) {
                    val initialStatus = async { getSingleStatus(key) }
                    val additionalStatuses = async { getStatusList(maxId = key, limit = params.loadSize - 1) }
                    listOf(initialStatus.await()) + additionalStatuses.await()
                }
            } else {
                val maxId = if (params is LoadParams.Refresh || params is LoadParams.Append) {
                    params.key
                } else {
                    null
                }

                val minId = if (params is LoadParams.Prepend) {
                    params.key
                } else {
                    null
                }

                getStatusList(minId = minId, maxId = maxId, limit = params.loadSize)
            }
            return LoadResult.Page(
                data = result,
                prevKey = result.firstOrNull()?.id,
                nextKey = result.lastOrNull()?.id
            )
        } catch (e: Exception) {
            Timber.tag("StatusesPagingSource").w(e, "failed to load statuses")
            return LoadResult.Error(e)
        }
    }

    private suspend fun getSingleStatus(statusId: String): Status {
        return mastodonApi.statusObservable(statusId).await()
    }

    private suspend fun getStatusList(minId: String? = null, maxId: String? = null, limit: Int): List<Status> {
        return mastodonApi.accountStatusesObservable(
            accountId = accountId,
            maxId = maxId,
            sinceId = null,
            minId = minId,
            limit = limit,
            excludeReblogs = true
        ).await()
    }
}
