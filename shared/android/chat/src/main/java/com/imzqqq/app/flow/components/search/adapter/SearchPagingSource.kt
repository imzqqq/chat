package com.imzqqq.app.flow.components.search.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imzqqq.app.flow.components.search.SearchType
import com.imzqqq.app.flow.entity.SearchResult
import com.imzqqq.app.flow.network.MastodonApi
import kotlinx.coroutines.rx3.await

class SearchPagingSource<T : Any>(
    private val mastodonApi: MastodonApi,
    private val searchType: SearchType,
    private val searchRequest: String,
    private val initialItems: List<T>?,
    private val parser: (SearchResult) -> List<T>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        if (searchRequest.isEmpty()) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        if (params.key == null && !initialItems.isNullOrEmpty()) {
            return LoadResult.Page(
                data = initialItems.toList(),
                prevKey = null,
                nextKey = initialItems.size
            )
        }

        val currentKey = params.key ?: 0

        try {

            val data = mastodonApi.searchObservable(
                query = searchRequest,
                type = searchType.apiParameter,
                resolve = true,
                limit = params.loadSize,
                offset = currentKey,
                following = false
            ).await()

            val res = parser(data)

            val nextKey = if (res.isEmpty()) {
                null
            } else {
                currentKey + res.size
            }

            return LoadResult.Page(
                data = res,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
