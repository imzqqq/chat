package com.imzqqq.app.features.home.room.detail.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import com.airbnb.mvrx.Mavericks
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addFragment
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySearchBinding

@AndroidEntryPoint
class SearchActivity : VectorBaseActivity<ActivitySearchBinding>() {

    private val searchFragment: SearchFragment?
        get() {
            return supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as? SearchFragment
        }

    override fun getBinding() = ActivitySearchBinding.inflate(layoutInflater)

    override fun getCoordinatorLayout() = views.coordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureToolbar(views.searchToolbar)
    }

    override fun initUiAndData() {
        if (isFirstCreation()) {
            val fragmentArgs: SearchArgs = intent?.extras?.getParcelable(Mavericks.KEY_ARG) ?: return
            addFragment(R.id.searchFragmentContainer, SearchFragment::class.java, fragmentArgs, FRAGMENT_TAG)
        }
        views.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchFragment?.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
        // Open the keyboard immediately
        views.searchView.requestFocus()
    }

    companion object {
        private const val FRAGMENT_TAG = "SearchFragment"

        fun newIntent(context: Context, args: SearchArgs): Intent {
            return Intent(context, SearchActivity::class.java).apply {
                // If we do that we will have the same room two times on the stack. Let's allow infinite stack for the moment.
                // flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                putExtra(Mavericks.KEY_ARG, args)
            }
        }
    }
}
