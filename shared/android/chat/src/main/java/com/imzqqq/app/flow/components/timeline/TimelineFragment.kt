@file:Suppress("DEPRECATION")

package com.imzqqq.app.flow.components.timeline

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import at.connyduck.sparkbutton.helpers.Utils
import autodispose2.androidx.lifecycle.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.flow.AccountListActivity
import com.imzqqq.app.flow.AccountListActivity.Companion.newIntent
import com.imzqqq.app.flow.BaseActivity
import com.imzqqq.app.R
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.flow.appstore.EventHub
import com.imzqqq.app.flow.appstore.PreferenceChangedEvent
import com.imzqqq.app.databinding.FragmentTimelineBinding
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.interfaces.RefreshableFragment
import com.imzqqq.app.flow.interfaces.ReselectableFragment
import com.imzqqq.app.flow.interfaces.StatusActionListener
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.viewdata.AttachmentViewData
import com.imzqqq.app.flow.viewdata.StatusViewData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.imzqqq.app.databinding.ActivityListsBinding
import com.imzqqq.app.flow.util.*
import timber.log.Timber

@AndroidEntryPoint
class TimelineFragment :
        com.imzqqq.app.flow.fragment.SFragment(),
        OnRefreshListener,
        StatusActionListener,
        ReselectableFragment,
        RefreshableFragment {

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory
    @Inject lateinit var eventHub: EventHub
    @Inject lateinit var accountManager: AccountManager

    private val viewModel: TimelineViewModel by viewModels { flowViewModelFactory }
    private lateinit var binding: FragmentTimelineBinding

    private lateinit var adapter: TimelineAdapter

    private var isSwipeToRefreshEnabled = true

    private var eventRegistered = false

    private var layoutManager: LinearLayoutManager? = null
    private var scrollListener: com.imzqqq.app.flow.view.EndlessOnScrollListener? = null
    private var hideFab = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = requireArguments()
        val kind = TimelineViewModel.Kind.valueOf(arguments.getString(KIND_ARG)!!)
        val id: String? = if (kind == TimelineViewModel.Kind.USER ||
            kind == TimelineViewModel.Kind.USER_PINNED ||
            kind == TimelineViewModel.Kind.USER_WITH_REPLIES ||
            kind == TimelineViewModel.Kind.LIST
        ) {
            arguments.getString(ID_ARG)!!
        } else {
            null
        }

        val tags = if (kind == TimelineViewModel.Kind.TAG) {
            arguments.getStringArrayList(HASHTAGS_ARG)!!
        } else {
            listOf()
        }
        viewModel.init(
            kind,
            id,
            tags,
        )

        viewModel.viewUpdates
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe { this.updateViews() }

        isSwipeToRefreshEnabled = arguments.getBoolean(ARG_ENABLE_SWIPE_TO_REFRESH, true)

        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val statusDisplayOptions = StatusDisplayOptions(
            animateAvatars = preferences.getBoolean(PrefKeys.ANIMATE_GIF_AVATARS, false),
            mediaPreviewEnabled = accountManager.activeAccount!!.mediaPreviewEnabled,
            useAbsoluteTime = preferences.getBoolean(PrefKeys.ABSOLUTE_TIME_VIEW, false),
            showBotOverlay = preferences.getBoolean(PrefKeys.SHOW_BOT_OVERLAY, true),
            useBlurhash = preferences.getBoolean(PrefKeys.USE_BLURHASH, true),
            cardViewMode = if (preferences.getBoolean(
                    PrefKeys.SHOW_CARDS_IN_TIMELINES,
                    false
                )
            ) CardViewMode.INDENTED else CardViewMode.NONE,
            confirmReblogs = preferences.getBoolean(PrefKeys.CONFIRM_REBLOGS, true),
            confirmFavourites = preferences.getBoolean(PrefKeys.CONFIRM_FAVOURITES, true),
            hideStats = preferences.getBoolean(PrefKeys.WELLBEING_HIDE_STATS_POSTS, false),
            animateEmojis = preferences.getBoolean(PrefKeys.ANIMATE_CUSTOM_EMOJIS, false)
        )
        adapter = TimelineAdapter(
            dataSource,
            statusDisplayOptions,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSwipeRefreshLayout()
        setupRecyclerView()
        updateViews()
        viewModel.loadInitial()
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.isEnabled = isSwipeToRefreshEnabled
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.tusky_blue)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.setAccessibilityDelegateCompat(
            ListStatusAccessibilityDelegate(binding.recyclerView, this) { pos -> viewModel.statuses.getOrNull(pos) }
        )
        binding.recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        val divider = DividerItemDecoration(context, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(divider)

        // CWs are expanded without animation, buttons animate itself, we don't need it basically
        (binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.recyclerView.adapter = adapter
    }

    private fun showEmptyView() {
        binding.statusView.show()
        binding.statusView.setup(R.drawable.elephant_friend_empty, R.string.message_empty, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* This is delayed until onActivityCreated solely because MainActivity.composeButton isn't
         * guaranteed to be set until then. */
        scrollListener = if (actionButtonPresent()) {
            /* Use a modified scroll listener that both loads more statuses as it goes, and hides
             * the follow button on down-scroll. */
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            hideFab = preferences.getBoolean("fabHide", false)
            object : com.imzqqq.app.flow.view.EndlessOnScrollListener(layoutManager) {
                override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(view, dx, dy)
                    val composeButton = (activity as com.imzqqq.app.flow.interfaces.ActionButtonActivity).actionButton
                    if (composeButton != null) {
                        if (hideFab) {
                            if (dy > 0 && composeButton.isShown) {
                                composeButton.hide() // hides the button if we're scrolling down
                            } else if (dy < 0 && !composeButton.isShown) {
                                composeButton.show() // shows it if we are scrolling up
                            }
                        } else if (!composeButton.isShown) {
                            composeButton.show()
                        }
                    }
                }

                override fun onLoadMore(totalItemsCount: Int, view: RecyclerView) {
                    this@TimelineFragment.onLoadMore()
                }
            }
        } else {
            // Just use the basic scroll listener to load more statuses.
            object : com.imzqqq.app.flow.view.EndlessOnScrollListener(layoutManager) {
                override fun onLoadMore(totalItemsCount: Int, view: RecyclerView) {
                    this@TimelineFragment.onLoadMore()
                }
            }
        }.also {
            binding.recyclerView.addOnScrollListener(it)
        }

        if (!eventRegistered) {
            eventHub.events
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this, Lifecycle.Event.ON_DESTROY)
                .subscribe { event ->
                    when (event) {
                        is PreferenceChangedEvent -> {
                            onPreferenceChanged(event.preferenceKey)
                        }
                    }
                }
            eventRegistered = true
        }
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isEnabled = isSwipeToRefreshEnabled
        binding.statusView.hide()

        viewModel.refresh()
    }

    override fun onReply(position: Int) {
        val status = viewModel.statuses[position].asStatusOrNull() ?: return
        super.reply(status.status)
    }

    override fun onReblog(reblog: Boolean, position: Int) {
        viewModel.reblog(reblog, position)
    }

    override fun onFavourite(favourite: Boolean, position: Int) {
        viewModel.favorite(favourite, position)
    }

    override fun onBookmark(bookmark: Boolean, position: Int) {
        viewModel.bookmark(bookmark, position)
    }

    override fun onVoteInPoll(position: Int, choices: List<Int>) {
        viewModel.voteInPoll(position, choices)
    }

    override fun onMore(view: View, position: Int) {
        val status = viewModel.statuses[position].asStatusOrNull()?.status ?: return
        super.more(status, view, position)
    }

    override fun onOpenReblog(position: Int) {
        val status = viewModel.statuses[position].asStatusOrNull()?.status ?: return
        super.openReblog(status)
    }

    override fun onExpandedChange(expanded: Boolean, position: Int) {
        viewModel.changeExpanded(expanded, position)
        updateViews()
    }

    override fun onContentHiddenChange(isShowing: Boolean, position: Int) {
        viewModel.changeContentHidden(isShowing, position)
        updateViews()
    }

    override fun onShowReblogs(position: Int) {
        val statusId = viewModel.statuses[position].asStatusOrNull()?.id ?: return
        val intent = newIntent(requireContext(), AccountListActivity.Type.REBLOGGED, statusId)
        (activity as BaseActivity).startActivityWithSlideInAnimation(intent)
    }

    override fun onShowFavs(position: Int) {
        val statusId = viewModel.statuses[position].asStatusOrNull()?.id ?: return
        val intent = newIntent(requireContext(), AccountListActivity.Type.FAVOURITED, statusId)
        (activity as BaseActivity).startActivityWithSlideInAnimation(intent)
    }

    override fun onLoadMore(position: Int) {
        viewModel.loadGap(position)
    }

    override fun onContentCollapsedChange(isCollapsed: Boolean, position: Int) {
        viewModel.changeContentCollapsed(isCollapsed, position)
    }

    override fun onViewMedia(position: Int, attachmentIndex: Int, view: View?) {
        val status = viewModel.statuses[position].asStatusOrNull() ?: return
        super.viewMedia(
            attachmentIndex,
            AttachmentViewData.list(status.actionable),
            view
        )
    }

    override fun onViewThread(position: Int) {
        val status = viewModel.statuses[position].asStatusOrNull() ?: return
        super.viewThread(status.actionable.id, status.actionable.url)
    }

    override fun onViewTag(tag: String) {
        if (viewModel.kind == TimelineViewModel.Kind.TAG && viewModel.tags.size == 1 &&
            viewModel.tags.contains(tag)
        ) {
            // If already viewing a tag page, then ignore any request to view that tag again.
            return
        }
        super.viewTag(tag)
    }

    override fun onViewAccount(id: String) {
        if ((
            viewModel.kind == TimelineViewModel.Kind.USER ||
                viewModel.kind == TimelineViewModel.Kind.USER_WITH_REPLIES
            ) &&
            viewModel.id == id
        ) {
            /* If already viewing an account page, then any requests to view that account page
             * should be ignored. */
            return
        }
        super.viewAccount(id)
    }

    private fun onPreferenceChanged(key: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        when (key) {
            PrefKeys.FAB_HIDE              -> {
                hideFab = sharedPreferences.getBoolean(PrefKeys.FAB_HIDE, false)
            }
            PrefKeys.MEDIA_PREVIEW_ENABLED -> {
                val enabled = accountManager.activeAccount!!.mediaPreviewEnabled
                val oldMediaPreviewEnabled = adapter.mediaPreviewEnabled
                if (enabled != oldMediaPreviewEnabled) {
                    adapter.mediaPreviewEnabled = enabled
                    updateViews()
                }
            }
        }
    }

    public override fun removeItem(position: Int) {
        viewModel.statuses.removeAt(position)
        updateViews()
    }

    private fun onLoadMore() {
        viewModel.loadMore()
    }

    private fun actionButtonPresent(): Boolean {
        return viewModel.kind != TimelineViewModel.Kind.TAG &&
            viewModel.kind != TimelineViewModel.Kind.FAVOURITES &&
            viewModel.kind != TimelineViewModel.Kind.BOOKMARKS &&
            activity is com.imzqqq.app.flow.interfaces.ActionButtonActivity
    }

    private fun updateViews() {
        differ.submitList(viewModel.statuses.toList())
        binding.swipeRefreshLayout.isEnabled = viewModel.failure == null

        if (isAdded) {
            binding.swipeRefreshLayout.isRefreshing = viewModel.isRefreshing
            binding.progressBar.visible(viewModel.isLoadingInitially)
            if (viewModel.failure == null && viewModel.statuses.isEmpty() && !viewModel.isLoadingInitially) {
                showEmptyView()
            } else {
                when (viewModel.failure) {
                    TimelineViewModel.FailureReason.NETWORK -> {
                        binding.statusView.show()
                        binding.statusView.setup(
                            R.drawable.elephant_offline,
                            R.string.error_network
                        ) {
                            binding.statusView.hide()
                            viewModel.loadInitial()
                        }
                    }
                    TimelineViewModel.FailureReason.OTHER   -> {
                        binding.statusView.show()
                        binding.statusView.setup(
                            R.drawable.elephant_error,
                            R.string.error_generic
                        ) {
                            binding.statusView.hide()
                            viewModel.loadInitial()
                        }
                    }
                    null                                    -> binding.statusView.hide()
                }
            }
        }
    }

    private val listUpdateCallback: ListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            if (isAdded) {
                adapter.notifyItemRangeInserted(position, count)
                val context = context
                // scroll up when new items at the top are loaded while being in the first position
                // https://github.com/tuskyapp/Tusky/pull/1905#issuecomment-677819724
                if (position == 0 && context != null && adapter.itemCount != count) {
                    if (isSwipeToRefreshEnabled) {
                        binding.recyclerView.scrollBy(0, Utils.dpToPx(context, -30))
                    } else binding.recyclerView.scrollToPosition(0)
                }
            }
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyItemRangeRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }
    }
    private val differ = AsyncListDiffer(
        listUpdateCallback,
        AsyncDifferConfig.Builder(diffCallback).build()
    )

    private val dataSource: TimelineAdapter.AdapterDataSource<StatusViewData> =
        object : TimelineAdapter.AdapterDataSource<StatusViewData> {
            override fun getItemCount(): Int {
                return differ.currentList.size
            }

            override fun getItemAt(pos: Int): StatusViewData {
                return differ.currentList[pos]
            }
        }

    private var talkBackWasEnabled = false

    override fun onResume() {
        super.onResume()
        val a11yManager =
            ContextCompat.getSystemService(requireContext(), AccessibilityManager::class.java)

        val wasEnabled = talkBackWasEnabled
        talkBackWasEnabled = a11yManager?.isEnabled == true
        Timber.d("talkback was enabled: $wasEnabled, now $talkBackWasEnabled")
        if (talkBackWasEnabled && !wasEnabled) {
            adapter.notifyDataSetChanged()
        }
        startUpdateTimestamp()
    }

    /**
     * Start to update adapter every minute to refresh timestamp
     * If setting absoluteTimeView is false
     * Auto dispose observable on pause
     */
    private fun startUpdateTimestamp() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val useAbsoluteTime = preferences.getBoolean(PrefKeys.ABSOLUTE_TIME_VIEW, false)
        if (!useAbsoluteTime) {
            Observable.interval(1, TimeUnit.MINUTES)
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this, Lifecycle.Event.ON_PAUSE)
                .subscribe { updateViews() }
        }
    }

    override fun onReselect() {
        if (isAdded) {
            layoutManager!!.scrollToPosition(0)
            binding.recyclerView.stopScroll()
            scrollListener!!.reset()
        }
    }

    override fun refreshContent() {
        onRefresh()
    }

    companion object {
        private const val TAG = "TimelineF" // logging tag
        private const val KIND_ARG = "kind"
        private const val ID_ARG = "id"
        private const val HASHTAGS_ARG = "hashtags"
        private const val ARG_ENABLE_SWIPE_TO_REFRESH = "enableSwipeToRefresh"

        fun newInstance(
                kind: TimelineViewModel.Kind,
                hashtagOrId: String? = null,
                enableSwipeToRefresh: Boolean = true
        ): TimelineFragment {
            val fragment = TimelineFragment()
            val arguments = Bundle(3)
            arguments.putString(KIND_ARG, kind.name)
            arguments.putString(ID_ARG, hashtagOrId)
            arguments.putBoolean(ARG_ENABLE_SWIPE_TO_REFRESH, enableSwipeToRefresh)
            fragment.arguments = arguments
            return fragment
        }

        @JvmStatic
        fun newHashtagInstance(hashtags: List<String>): TimelineFragment {
            val fragment = TimelineFragment()
            val arguments = Bundle(3)
            arguments.putString(KIND_ARG, TimelineViewModel.Kind.TAG.name)
            arguments.putStringArrayList(HASHTAGS_ARG, ArrayList(hashtags))
            arguments.putBoolean(ARG_ENABLE_SWIPE_TO_REFRESH, true)
            fragment.arguments = arguments
            return fragment
        }

        private val diffCallback: DiffUtil.ItemCallback<StatusViewData> =
            object : DiffUtil.ItemCallback<StatusViewData>() {
                override fun areItemsTheSame(
                        oldItem: StatusViewData,
                        newItem: StatusViewData
                ): Boolean {
                    return oldItem.viewDataId == newItem.viewDataId
                }

                override fun areContentsTheSame(
                        oldItem: StatusViewData,
                        newItem: StatusViewData
                ): Boolean {
                    return false // Items are different always. It allows to refresh timestamp on every view holder update
                }

                override fun getChangePayload(
                        oldItem: StatusViewData,
                        newItem: StatusViewData
                ): Any? {
                    return if (oldItem === newItem) {
                        // If items are equal - update timestamp only
                        listOf(com.imzqqq.app.flow.adapter.StatusBaseViewHolder.Key.KEY_CREATED)
                    } else // If items are different - update the whole view holder
                        null
                }
            }
    }
}
