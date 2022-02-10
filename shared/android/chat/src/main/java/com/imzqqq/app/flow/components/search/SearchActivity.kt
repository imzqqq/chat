package com.imzqqq.app.flow.components.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.databinding.ActivityListsBinding
import com.imzqqq.app.databinding.ActivitySearchFlowBinding
import com.imzqqq.app.flow.BottomSheetActivity
import com.imzqqq.app.flow.components.search.adapter.SearchPagerAdapter
import com.imzqqq.app.flow.util.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : BottomSheetActivity() {

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory

    private val viewModel: SearchViewModel by viewModels { flowViewModelFactory }
    private lateinit var binding: ActivitySearchFlowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        setupPages()
        handleIntent(intent)
    }

    private fun setupPages() {
        binding.pages.adapter = SearchPagerAdapter(this)

        TabLayoutMediator(binding.tabs, binding.pages) {
            tab, position ->
            tab.text = getPageTitle(position)
        }.attach()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.search_toolbar, menu)
        val searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        setupSearchView(searchView)

        searchView.setQuery(viewModel.currentQuery, false)

        return true
    }

    private fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> getString(R.string.title_statuses)
            1 -> getString(R.string.title_accounts)
            2 -> getString(R.string.title_hashtags_dialog)
            else -> throw IllegalArgumentException("Unknown page index: $position")
        }
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            viewModel.currentQuery = intent.getStringExtra(SearchManager.QUERY) ?: ""
            viewModel.search(viewModel.currentQuery)
        }
    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.setIconifiedByDefault(false)

        searchView.setSearchableInfo((getSystemService(Context.SEARCH_SERVICE) as? SearchManager)?.getSearchableInfo(componentName))

        searchView.requestFocus()

        searchView.maxWidth = Integer.MAX_VALUE
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }
}
