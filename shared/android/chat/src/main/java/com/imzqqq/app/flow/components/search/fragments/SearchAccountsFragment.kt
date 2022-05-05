package com.imzqqq.app.flow.components.search.fragments

import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.flow.components.search.adapter.SearchAccountsAdapter
import com.imzqqq.app.flow.entity.Account
import com.imzqqq.app.flow.settings.PrefKeys
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class SearchAccountsFragment : SearchFragment<Account>() {
    override fun createAdapter(): PagingDataAdapter<Account, *> {
        val preferences = PreferenceManager.getDefaultSharedPreferences(binding.searchRecyclerView.context)

        return SearchAccountsAdapter(
            this,
            preferences.getBoolean(PrefKeys.ANIMATE_GIF_AVATARS, false),
            preferences.getBoolean(PrefKeys.ANIMATE_CUSTOM_EMOJIS, false)
        )
    }

    override val data: Flow<PagingData<Account>>
        get() = viewModel.accountsFlow

    companion object {
        fun newInstance() = SearchAccountsFragment()
    }
}
