package com.imzqqq.app.flow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ActivityStatuslistBinding
import com.imzqqq.app.flow.components.timeline.TimelineFragment
import com.imzqqq.app.flow.components.timeline.TimelineViewModel.Kind

@AndroidEntryPoint
class StatusListActivity : BottomSheetActivity() {

    private val kind: Kind
        get() = Kind.valueOf(intent.getStringExtra(EXTRA_KIND)!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStatuslistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)

        val title = if (kind == Kind.FAVOURITES) {
            R.string.title_favourites
        } else {
            R.string.title_bookmarks
        }

        supportActionBar?.run {
            setTitle(title)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        supportFragmentManager.commit {
            val fragment = TimelineFragment.newInstance(kind)
            replace(R.id.fragment_container, fragment)
        }
    }

    companion object {

        private const val EXTRA_KIND = "kind"

        @JvmStatic
        fun newFavouritesIntent(context: Context) =
            Intent(context, StatusListActivity::class.java).apply {
                putExtra(EXTRA_KIND, Kind.FAVOURITES.name)
            }

        @JvmStatic
        fun newBookmarksIntent(context: Context) =
            Intent(context, StatusListActivity::class.java).apply {
                putExtra(EXTRA_KIND, Kind.BOOKMARKS.name)
            }
    }
}
