package com.imzqqq.app.flow.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import autodispose2.androidx.lifecycle.autoDispose
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ActivityListsBinding
import com.imzqqq.app.flow.ViewMediaActivity
import com.imzqqq.app.databinding.FragmentTimelineBinding
import com.imzqqq.app.flow.entity.Attachment
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.interfaces.RefreshableFragment
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.util.LinkHelper
import com.imzqqq.app.flow.util.ThemeUtils
import com.imzqqq.app.flow.util.hide
import com.imzqqq.app.flow.util.show
import com.imzqqq.app.flow.util.viewBinding
import com.imzqqq.app.flow.view.SquareImageView
import com.imzqqq.app.flow.viewdata.AttachmentViewData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.util.Random
import javax.inject.Inject

/**
 * Fragment with multiple columns of media previews for the specified account.
 */
@AndroidEntryPoint
class AccountMediaFragment : Fragment(R.layout.fragment_timeline), RefreshableFragment {

    @Inject lateinit var api: MastodonApi

    private lateinit var binding: FragmentTimelineBinding

    private lateinit var accountId: String

    private val adapter = MediaGridAdapter()
    private val statuses = mutableListOf<Status>()
    private var fetchingStatus = FetchingStatus.NOT_FETCHING

    private var isSwipeToRefreshEnabled: Boolean = true
    private var needToRefresh = false

    private val callback = object : SingleObserver<Response<List<Status>>> {
        override fun onError(t: Throwable) {
            fetchingStatus = FetchingStatus.NOT_FETCHING

            if (isAdded) {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressBar.visibility = View.GONE
                binding.topProgressBar.hide()
                binding.statusView.show()
                if (t is IOException) {
                    binding.statusView.setup(R.drawable.elephant_offline, R.string.error_network) {
                        doInitialLoadingIfNeeded()
                    }
                } else {
                    binding.statusView.setup(R.drawable.elephant_error, R.string.error_generic) {
                        doInitialLoadingIfNeeded()
                    }
                }
            }

            Timber.d(t, "Failed to fetch account media")
        }

        override fun onSuccess(response: Response<List<Status>>) {
            fetchingStatus = FetchingStatus.NOT_FETCHING
            if (isAdded) {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressBar.visibility = View.GONE
                binding.topProgressBar.hide()

                val body = response.body()
                body?.let { fetched ->
                    statuses.addAll(0, fetched)
                    // flatMap requires iterable but I don't want to box each array into list
                    val result = mutableListOf<AttachmentViewData>()
                    for (status in fetched) {
                        result.addAll(AttachmentViewData.list(status))
                    }
                    adapter.addTop(result)
                    if (result.isNotEmpty())
                        binding.recyclerView.scrollToPosition(0)

                    if (statuses.isEmpty()) {
                        binding.statusView.show()
                        binding.statusView.setup(R.drawable.elephant_friend_empty, R.string.message_empty)
                    }
                }
            }
        }

        override fun onSubscribe(d: Disposable) {}
    }

    private val bottomCallback = object : SingleObserver<Response<List<Status>>> {
        override fun onError(t: Throwable) {
            fetchingStatus = FetchingStatus.NOT_FETCHING

            Timber.d(t, "Failed to fetch account media")
        }

        override fun onSuccess(response: Response<List<Status>>) {
            fetchingStatus = FetchingStatus.NOT_FETCHING
            val body = response.body()
            body?.let { fetched ->
                Timber.d("fetched " + fetched.size + " statuses")
                if (fetched.isNotEmpty()) Timber.d("first: " + fetched.first().id + ", last: " + fetched.last().id)
                statuses.addAll(fetched)
                Timber.d("now there are " + statuses.size + " statuses")
                // flatMap requires iterable but I don't want to box each array into list
                val result = mutableListOf<AttachmentViewData>()
                for (status in fetched) {
                    result.addAll(AttachmentViewData.list(status))
                }
                adapter.addBottom(result)
            }
        }

        override fun onSubscribe(d: Disposable) { }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSwipeToRefreshEnabled = arguments?.getBoolean(ARG_ENABLE_SWIPE_TO_REFRESH, true) == true
        accountId = arguments?.getString(ACCOUNT_ID_ARG)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val columnCount = view.context.resources.getInteger(R.integer.profile_media_column_count)
        val layoutManager = GridLayoutManager(view.context, columnCount)

        adapter.baseItemColor = ThemeUtils.getColor(view.context, android.R.attr.windowBackground)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        if (isSwipeToRefreshEnabled) {
            binding.swipeRefreshLayout.setOnRefreshListener {
                refresh()
            }
            binding.swipeRefreshLayout.setColorSchemeResources(R.color.tusky_blue)
        }
        binding.statusView.visibility = View.GONE

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recycler_view: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val itemCount = layoutManager.itemCount
                    val lastItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (itemCount <= lastItem + 3 && fetchingStatus == FetchingStatus.NOT_FETCHING) {
                        statuses.lastOrNull()?.let { (id) ->
                            Timber.d("Requesting statuses with max_id: $id, (bottom)")
                            fetchingStatus = FetchingStatus.FETCHING_BOTTOM
                            api.accountStatuses(accountId, id, null, null, null, true, null)
                                .observeOn(AndroidSchedulers.mainThread())
                                .autoDispose(this@AccountMediaFragment, Lifecycle.Event.ON_DESTROY)
                                .subscribe(bottomCallback)
                        }
                    }
                }
            }
        })

        doInitialLoadingIfNeeded()
    }

    private fun refresh() {
        binding.statusView.hide()
        if (fetchingStatus != FetchingStatus.NOT_FETCHING) return
        if (statuses.isEmpty()) {
            fetchingStatus = FetchingStatus.INITIAL_FETCHING
            api.accountStatuses(accountId, null, null, null, null, true, null)
        } else {
            fetchingStatus = FetchingStatus.REFRESHING
            api.accountStatuses(accountId, null, statuses[0].id, null, null, true, null)
        }.observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this, Lifecycle.Event.ON_DESTROY)
            .subscribe(callback)

        if (!isSwipeToRefreshEnabled)
            binding.topProgressBar.show()
    }

    private fun doInitialLoadingIfNeeded() {
        if (isAdded) {
            binding.statusView.hide()
        }
        if (fetchingStatus == FetchingStatus.NOT_FETCHING && statuses.isEmpty()) {
            fetchingStatus = FetchingStatus.INITIAL_FETCHING
            api.accountStatuses(accountId, null, null, null, null, true, null)
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this@AccountMediaFragment, Lifecycle.Event.ON_DESTROY)
                .subscribe(callback)
        } else if (needToRefresh)
            refresh()
        needToRefresh = false
    }

    private fun viewMedia(items: List<AttachmentViewData>, currentIndex: Int, view: View?) {

        when (items[currentIndex].attachment.type) {
            Attachment.Type.IMAGE,
            Attachment.Type.GIFV,
            Attachment.Type.VIDEO,
            Attachment.Type.AUDIO   -> {
                val intent = ViewMediaActivity.newIntent(context, items, currentIndex)
                if (view != null && activity != null) {
                    val url = items[currentIndex].attachment.url
                    ViewCompat.setTransitionName(view, url)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), view, url)
                    startActivity(intent, options.toBundle())
                } else {
                    startActivity(intent)
                }
            }
            Attachment.Type.UNKNOWN -> {
                LinkHelper.openLink(items[currentIndex].attachment.url, context)
            }
        }
    }

    private enum class FetchingStatus {
        NOT_FETCHING, INITIAL_FETCHING, FETCHING_BOTTOM, REFRESHING
    }

    inner class MediaGridAdapter :
        RecyclerView.Adapter<MediaGridAdapter.MediaViewHolder>() {

        var baseItemColor = Color.BLACK

        private val items = mutableListOf<AttachmentViewData>()
        private val itemBgBaseHSV = FloatArray(3)
        private val random = Random()

        fun addTop(newItems: List<AttachmentViewData>) {
            items.addAll(0, newItems)
            notifyItemRangeInserted(0, newItems.size)
        }

        fun addBottom(newItems: List<AttachmentViewData>) {
            if (newItems.isEmpty()) return

            val oldLen = items.size
            items.addAll(newItems)
            notifyItemRangeInserted(oldLen, newItems.size)
        }

        override fun onAttachedToRecyclerView(recycler_view: RecyclerView) {
            val hsv = FloatArray(3)
            Color.colorToHSV(baseItemColor, hsv)
            super.onAttachedToRecyclerView(recycler_view)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
            val view = SquareImageView(parent.context)
            view.scaleType = ImageView.ScaleType.CENTER_CROP
            return MediaViewHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
            itemBgBaseHSV[2] = random.nextFloat() * (1f - 0.3f) + 0.3f
            holder.imageView.setBackgroundColor(Color.HSVToColor(itemBgBaseHSV))
            val item = items[position]

            Glide.with(holder.imageView)
                .load(item.attachment.previewUrl)
                .centerInside()
                .into(holder.imageView)
        }

        inner class MediaViewHolder(val imageView: ImageView) :
            RecyclerView.ViewHolder(imageView),
            View.OnClickListener {
            init {
                itemView.setOnClickListener(this)
            }

            // saving some allocations
            override fun onClick(v: View?) {
                viewMedia(items, bindingAdapterPosition, imageView)
            }
        }
    }

    override fun refreshContent() {
        if (isAdded)
            refresh()
        else
            needToRefresh = true
    }

    companion object {
        @JvmStatic
        fun newInstance(accountId: String, enableSwipeToRefresh: Boolean = true): AccountMediaFragment {
            val fragment = AccountMediaFragment()
            val args = Bundle()
            args.putString(ACCOUNT_ID_ARG, accountId)
            args.putBoolean(ARG_ENABLE_SWIPE_TO_REFRESH, enableSwipeToRefresh)
            fragment.arguments = args
            return fragment
        }

        private const val ACCOUNT_ID_ARG = "account_id"
        private const val TAG = "AccountMediaFragment"
        private const val ARG_ENABLE_SWIPE_TO_REFRESH = "arg.enable.swipe.to.refresh"
    }
}
