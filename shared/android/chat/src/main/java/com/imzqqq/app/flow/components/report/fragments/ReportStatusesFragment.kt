@file:Suppress("DEPRECATION")

package com.imzqqq.app.flow.components.report.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.paging.LoadState
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.databinding.FragmentReportStatusesBinding
import com.imzqqq.app.flow.AccountActivity
import com.imzqqq.app.flow.ViewMediaActivity
import com.imzqqq.app.flow.ViewTagActivity
import com.imzqqq.app.flow.components.report.ReportViewModel
import com.imzqqq.app.flow.components.report.Screen
import com.imzqqq.app.flow.components.report.adapter.AdapterHandler
import com.imzqqq.app.flow.components.report.adapter.StatusesAdapter
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.entity.Attachment
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.util.CardViewMode
import com.imzqqq.app.flow.util.StatusDisplayOptions
import com.imzqqq.app.flow.util.visible
import com.imzqqq.app.flow.viewdata.AttachmentViewData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReportStatusesFragment : Fragment(R.layout.fragment_report_statuses), AdapterHandler {

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory
    @Inject lateinit var accountManager: AccountManager

    private val viewModel: ReportViewModel by activityViewModels { flowViewModelFactory }
    private lateinit var binding: FragmentReportStatusesBinding

    private lateinit var adapter: StatusesAdapter

    private var snackbarErrorRetry: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReportStatusesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun showMedia(v: View?, status: Status?, idx: Int) {
        status?.actionableStatus?.let { actionable ->
            when (actionable.attachments[idx].type) {
                Attachment.Type.GIFV, Attachment.Type.VIDEO, Attachment.Type.IMAGE, Attachment.Type.AUDIO -> {
                    val attachments = AttachmentViewData.list(actionable)
                    val intent = ViewMediaActivity.newIntent(context, attachments, idx)
                    if (v != null) {
                        val url = actionable.attachments[idx].url
                        ViewCompat.setTransitionName(v, url)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), v, url)
                        startActivity(intent, options.toBundle())
                    } else {
                        startActivity(intent)
                    }
                }
                Attachment.Type.UNKNOWN                                                                   -> {
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleClicks()
        initStatusesView()
        setupSwipeRefreshLayout()
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.tusky_blue)

        binding.swipeRefreshLayout.setOnRefreshListener {
            snackbarErrorRetry?.dismiss()
            adapter.refresh()
        }
    }

    private fun initStatusesView() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val statusDisplayOptions = StatusDisplayOptions(
            animateAvatars = false,
            mediaPreviewEnabled = accountManager.activeAccount?.mediaPreviewEnabled ?: true,
            useAbsoluteTime = preferences.getBoolean("absoluteTimeView", false),
            showBotOverlay = false,
            useBlurhash = preferences.getBoolean("useBlurhash", true),
            cardViewMode = CardViewMode.NONE,
            confirmReblogs = preferences.getBoolean("confirmReblogs", true),
            confirmFavourites = preferences.getBoolean("confirmFavourites", true),
            hideStats = preferences.getBoolean(PrefKeys.WELLBEING_HIDE_STATS_POSTS, false),
            animateEmojis = preferences.getBoolean(PrefKeys.ANIMATE_CUSTOM_EMOJIS, false)
        )

        adapter = StatusesAdapter(statusDisplayOptions, viewModel.statusViewState, this)

        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        (binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        lifecycleScope.launch {
            viewModel.statusesFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Error ||
                loadState.append is LoadState.Error ||
                loadState.prepend is LoadState.Error
            ) {
                showError()
            }

            binding.progressBarBottom.visible(loadState.append == LoadState.Loading)
            binding.progressBarTop.visible(loadState.prepend == LoadState.Loading)
            binding.progressBarLoading.visible(loadState.refresh == LoadState.Loading && !binding.swipeRefreshLayout.isRefreshing)

            if (loadState.refresh != LoadState.Loading) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun showError() {
        if (snackbarErrorRetry?.isShown != true) {
            snackbarErrorRetry = Snackbar.make(binding.swipeRefreshLayout, R.string.failed_fetch_statuses, Snackbar.LENGTH_INDEFINITE)
            snackbarErrorRetry?.setAction(R.string.action_retry) {
                adapter.retry()
            }
            snackbarErrorRetry?.show()
        }
    }

    private fun handleClicks() {
        binding.buttonCancel.setOnClickListener {
            viewModel.navigateTo(Screen.Back)
        }

        binding.buttonContinue.setOnClickListener {
            viewModel.navigateTo(Screen.Note)
        }
    }

    override fun setStatusChecked(status: Status, isChecked: Boolean) {
        viewModel.setStatusChecked(status, isChecked)
    }

    override fun isStatusChecked(id: String): Boolean {
        return viewModel.isStatusChecked(id)
    }

    override fun onViewAccount(id: String) = startActivity(AccountActivity.getIntent(requireContext(), id))

    override fun onViewTag(tag: String) = startActivity(ViewTagActivity.getIntent(requireContext(), tag))

    override fun onViewUrl(url: String?) = viewModel.checkClickedUrl(url)

    companion object {
        fun newInstance() = ReportStatusesFragment()
    }
}
