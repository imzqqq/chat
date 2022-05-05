/* Copyright 2021 Flow Contributors
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

package com.keylesspalace.flow.components.search.adapter

import com.keylesspalace.flow.components.search.SearchType
import com.keylesspalace.flow.entity.SearchResult
import com.keylesspalace.flow.network.FlowApi

class SearchPagingSourceFactory<T : Any>(
    private val flowApi: FlowApi,
    private val searchType: SearchType,
    private val initialItems: List<T>? = null,
    private val parser: (SearchResult) -> List<T>
) : () -> SearchPagingSource<T> {

    private var searchRequest: String = ""

    private var currentSource: SearchPagingSource<T>? = null

    override fun invoke(): SearchPagingSource<T> {
        return SearchPagingSource(
            flowApi = flowApi,
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
