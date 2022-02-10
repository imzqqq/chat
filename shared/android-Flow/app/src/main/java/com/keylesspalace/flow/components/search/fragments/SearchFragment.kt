package com.keylesspalace.flow.components.search.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.keylesspalace.flow.AccountActivity
import com.keylesspalace.flow.BottomSheetActivity
import com.keylesspalace.flow.R
import com.keylesspalace.flow.ViewTagActivity
import com.keylesspalace.flow.components.search.SearchViewModel
import com.keylesspalace.flow.databinding.FragmentSearchBinding
import com.keylesspalace.flow.di.Injectable
import com.keylesspalace.flow.di.ViewModelFactory
import com.keylesspalace.flow.interfaces.LinkListener
import com.keylesspalace.flow.util.viewBinding
import com.keylesspalace.flow.util.visible
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class SearchFragment<T : Any> :
    Fragment(R.layout.fragment_search),
    LinkListener,
    Injectable,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected val viewModel: SearchViewModel by activityViewModels { viewModelFactory }

    protected val binding by viewBinding(FragmentSearchBinding::bind)

    private var snackbarErrorRetry: Snackbar? = null

    abstract fun createAdapter(): PagingDataAdapter<T, *>

    abstract val data: Flow<PagingData<T>>
    protected lateinit var adapter: PagingDataAdapter<T, *>

    private var currentQuery: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        setupSwipeRefreshLayout()
        subscribeObservables()
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.flow_blue)
    }

    private fun subscribeObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            data.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        adapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Error) {
                showError()
            }

            val isNewSearch = currentQuery != viewModel.currentQuery

            binding.searchProgressBar.visible(loadState.refresh == LoadState.Loading && isNewSearch && !binding.swipeRefreshLayout.isRefreshing)
            binding.searchRecyclerView.visible(loadState.refresh is LoadState.NotLoading || !isNewSearch || binding.swipeRefreshLayout.isRefreshing)

            if (loadState.refresh != LoadState.Loading) {
                binding.swipeRefreshLayout.isRefreshing = false
                currentQuery = viewModel.currentQuery
            }

            binding.progressBarBottom.visible(loadState.append == LoadState.Loading)

            binding.searchNoResultsText.visible(loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0 && viewModel.currentQuery.isNotEmpty())
        }
    }

    private fun initAdapter() {
        binding.searchRecyclerView.addItemDecoration(DividerItemDecoration(binding.searchRecyclerView.context, DividerItemDecoration.VERTICAL))
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(binding.searchRecyclerView.context)
        adapter = createAdapter()
        binding.searchRecyclerView.adapter = adapter
        binding.searchRecyclerView.setHasFixedSize(true)
        (binding.searchRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun showError() {
        if (snackbarErrorRetry?.isShown != true) {
            snackbarErrorRetry = Snackbar.make(binding.root, R.string.failed_search, Snackbar.LENGTH_INDEFINITE)
            snackbarErrorRetry?.setAction(R.string.action_retry) {
                snackbarErrorRetry = null
                adapter.retry()
            }
            snackbarErrorRetry?.show()
        }
    }

    override fun onViewAccount(id: String) = startActivity(AccountActivity.getIntent(requireContext(), id))

    override fun onViewTag(tag: String) = startActivity(ViewTagActivity.getIntent(requireContext(), tag))

    override fun onViewUrl(url: String) {
        bottomSheetActivity?.viewUrl(url)
    }

    protected val bottomSheetActivity
        get() = (activity as? BottomSheetActivity)

    override fun onRefresh() {
        adapter.refresh()
    }
}
