package com.imzqqq.app.flow.components.search.adapter

import com.imzqqq.app.flow.components.search.SearchType
import com.imzqqq.app.flow.entity.SearchResult
import com.imzqqq.app.flow.network.MastodonApi

class SearchPagingSourceFactory<T : Any>(
        private val mastodonApi: MastodonApi,
        private val searchType: SearchType,
        private val initialItems: List<T>? = null,
        private val parser: (SearchResult) -> List<T>
) : () -> SearchPagingSource<T> {

    private var searchRequest: String = ""

    private var currentSource: SearchPagingSource<T>? = null

    override fun invoke(): SearchPagingSource<T> {
        return SearchPagingSource(
            mastodonApi = mastodonApi,
            searchType = searchType,
            searchRequest = searchRequest,
            initialItems = initialItems,
            parser = parser
        ).also { source ->
            currentSource = source
        }
    }

    fun newSearch(newSearchRequest: String) {
        this.searchRequest = newSearchRequest
        currentSource?.invalidate()
    }

    fun invalidate() {
        currentSource?.invalidate()
    }
}
