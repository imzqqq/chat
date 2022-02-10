package com.imzqqq.app.flow.components.scheduled

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import autodispose2.androidx.lifecycle.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.flow.BaseActivity
import com.imzqqq.app.R
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.flow.appstore.EventHub
import com.imzqqq.app.flow.components.compose.ComposeActivity
import com.imzqqq.app.databinding.ActivityScheduledTootBinding
import com.imzqqq.app.flow.appstore.StatusScheduledEvent
import com.imzqqq.app.flow.entity.ScheduledStatus
import com.imzqqq.app.flow.util.hide
import com.imzqqq.app.flow.util.show
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScheduledTootActivity : BaseActivity(), ScheduledTootActionListener {

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory
    @Inject lateinit var eventHub: EventHub

    private val viewModel: ScheduledTootViewModel by viewModels { flowViewModelFactory }

    private val adapter = ScheduledTootAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityScheduledTootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.run {
            title = getString(R.string.title_scheduled_toot)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this::refreshStatuses)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.tusky_blue)

        binding.scheduledTootList.setHasFixedSize(true)
        binding.scheduledTootList.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.scheduledTootList.addItemDecoration(divider)
        binding.scheduledTootList.adapter = adapter

        lifecycleScope.launch {
            viewModel.data.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is Error) {
                binding.progressBar.hide()
                binding.errorMessageView.setup(R.drawable.elephant_error, R.string.error_generic) {
                    refreshStatuses()
                }
                binding.errorMessageView.show()
            }
            if (loadState.refresh != LoadState.Loading) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
            if (loadState.refresh is LoadState.NotLoading) {
                binding.progressBar.hide()
                if (adapter.itemCount == 0) {
                    binding.errorMessageView.setup(R.drawable.elephant_friend_empty, R.string.no_scheduled_status)
                    binding.errorMessageView.show()
                } else {
                    binding.errorMessageView.hide()
                }
            }
        }

        eventHub.events
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe { event ->
                if (event is StatusScheduledEvent) {
                    adapter.refresh()
                }
            }
    }

    private fun refreshStatuses() {
        adapter.refresh()
    }

    override fun edit(item: ScheduledStatus) {
        val intent = ComposeActivity.startIntent(
            this,
            ComposeActivity.ComposeOptions(
                scheduledTootId = item.id,
                tootText = item.params.text,
                contentWarning = item.params.spoilerText,
                mediaAttachments = item.mediaAttachments,
                inReplyToId = item.params.inReplyToId,
                visibility = item.params.visibility,
                scheduledAt = item.scheduledAt,
                sensitive = item.params.sensitive
            )
        )
        startActivity(intent)
    }

    override fun delete(item: ScheduledStatus) {
        viewModel.deleteScheduledStatus(item)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ScheduledTootActivity::class.java)
    }
}
