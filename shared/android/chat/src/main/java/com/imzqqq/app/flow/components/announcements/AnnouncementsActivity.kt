package com.imzqqq.app.flow.components.announcements

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.flow.BottomSheetActivity
import com.imzqqq.app.R
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.flow.ViewTagActivity
import com.imzqqq.app.flow.adapter.EmojiAdapter
import com.imzqqq.app.flow.adapter.OnEmojiSelectedListener
import com.imzqqq.app.databinding.ActivityAnnouncementsBinding
import com.imzqqq.app.databinding.ActivityListsBinding
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.util.*
import com.imzqqq.app.flow.view.EmojiPicker
import javax.inject.Inject

@AndroidEntryPoint
class AnnouncementsActivity : BottomSheetActivity(),
        AnnouncementActionListener,
        OnEmojiSelectedListener {

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory
    private val viewModel: AnnouncementsViewModel by viewModels { flowViewModelFactory }

    private lateinit var binding: ActivityAnnouncementsBinding
    private lateinit var adapter: AnnouncementAdapter

    private val picker by lazy { EmojiPicker(this) }
    private val pickerDialog by lazy {
        PopupWindow(this)
            .apply {
                contentView = picker
                isFocusable = true
                setOnDismissListener {
                    currentAnnouncementId = null
                }
            }
    }
    private var currentAnnouncementId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.title_announcements)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this::refreshAnnouncements)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.tusky_blue)

        binding.announcementsList.setHasFixedSize(true)
        binding.announcementsList.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.announcementsList.addItemDecoration(divider)

        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val wellbeingEnabled = preferences.getBoolean(PrefKeys.WELLBEING_HIDE_STATS_POSTS, false)
        val animateEmojis = preferences.getBoolean(PrefKeys.ANIMATE_CUSTOM_EMOJIS, false)

        adapter = AnnouncementAdapter(emptyList(), this, wellbeingEnabled, animateEmojis)

        binding.announcementsList.adapter = adapter

        viewModel.announcements.observe(this) {
            when (it) {
                is Success -> {
                    binding.progressBar.hide()
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (it.data.isNullOrEmpty()) {
                        binding.errorMessageView.setup(R.drawable.elephant_friend_empty, R.string.no_announcements)
                        binding.errorMessageView.show()
                    } else {
                        binding.errorMessageView.hide()
                    }
                    adapter.updateList(it.data ?: listOf())
                }
                is Loading -> {
                    binding.errorMessageView.hide()
                }
                is Error   -> {
                    binding.progressBar.hide()
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.errorMessageView.setup(R.drawable.elephant_error, R.string.error_generic) {
                        refreshAnnouncements()
                    }
                    binding.errorMessageView.show()
                }
            }
        }

        viewModel.emojis.observe(this) {
            picker.adapter = EmojiAdapter(it, this)
        }

        viewModel.load()
        binding.progressBar.show()
    }

    private fun refreshAnnouncements() {
        viewModel.load()
        binding.swipeRefreshLayout.isRefreshing = true
    }

    override fun openReactionPicker(announcementId: String, target: View) {
        currentAnnouncementId = announcementId
        pickerDialog.showAsDropDown(target)
    }

    override fun onEmojiSelected(shortcode: String) {
        viewModel.addReaction(currentAnnouncementId!!, shortcode)
        pickerDialog.dismiss()
    }

    override fun addReaction(announcementId: String, name: String) {
        viewModel.addReaction(announcementId, name)
    }

    override fun removeReaction(announcementId: String, name: String) {
        viewModel.removeReaction(announcementId, name)
    }

    override fun onViewTag(tag: String?) {
        val intent = Intent(this, ViewTagActivity::class.java)
        intent.putExtra("hashtag", tag)
        startActivityWithSlideInAnimation(intent)
    }

    override fun onViewAccount(id: String?) {
        if (id != null) {
            viewAccount(id)
        }
    }

    override fun onViewUrl(url: String?) {
        if (url != null) {
            viewUrl(url)
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AnnouncementsActivity::class.java)
    }
}
