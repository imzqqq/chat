package com.imzqqq.app.flow.components.search.fragments

import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.flow.components.search.adapter.SearchHashtagsAdapter
import com.imzqqq.app.flow.entity.HashTag
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class SearchHashtagsFragment : SearchFragment<HashTag>() {

    override val data: Flow<PagingData<HashTag>>
        get() = viewModel.hashtagsFlow

    override fun createAdapter(): PagingDataAdapter<HashTag, *> = SearchHashtagsAdapter(this)

    companion object {
        fun newInstance() = SearchHashtagsFragment()
    }
}
