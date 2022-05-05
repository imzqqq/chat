/* Copyright 2019 Joel Pyska
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

package com.keylesspalace.flow.components.report.fragments

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.keylesspalace.flow.AccountActivity
import com.keylesspalace.flow.R
import com.keylesspalace.flow.ViewMediaActivity
import com.keylesspalace.flow.ViewTagActivity
import com.keylesspalace.flow.components.report.ReportViewModel
import com.keylesspalace.flow.components.report.Screen
import com.keylesspalace.flow.components.report.adapter.AdapterHandler
import com.keylesspalace.flow.components.report.adapter.StatusesAdapter
import com.keylesspalace.flow.databinding.FragmentReportStatusesBinding
import com.keylesspalace.flow.db.AccountManager
import com.keylesspalace.flow.di.Injectable
import com.keylesspalace.flow.di.ViewModelFactory
import com.keylesspalace.flow.entity.Attachment
import com.keylesspalace.flow.entity.Status
import com.keylesspalace.flow.settings.PrefKeys
import com.keylesspalace.flow.util.CardViewMode
import com.keylesspalace.flow.util.StatusDisplayOptions
import com.keylesspalace.flow.util.viewBinding
import com.keylesspalace.flow.util.visible
import com.keylesspalace.flow.viewdata.AttachmentViewData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReportStatusesFragment : Fragment(R.layout.fragment_report_statuses), Injectable, AdapterHandler {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var accountManager: AccountManager

    private val viewModel: ReportViewModel by activityViewModels { viewModelFactory }

    private val binding by viewBinding(FragmentReportStatusesBinding::bind)

    private lateinit var adapter: StatusesAdapter

    private var snackbarErrorRetry: Snackbar? = null

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
                Attachment.Type.UNKNOWN -> {
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
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.flow_blue)

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
