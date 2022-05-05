package com.imzqqq.app.flow.components.search.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.imzqqq.app.flow.components.search.fragments.SearchAccountsFragment
import com.imzqqq.app.flow.components.search.fragments.SearchHashtagsFragment
import com.imzqqq.app.flow.components.search.fragments.SearchStatusesFragment

class SearchPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchStatusesFragment.newInstance()
            1 -> SearchAccountsFragment.newInstance()
            2 -> SearchHashtagsFragment.newInstance()
            else -> throw IllegalArgumentException("Unknown page index: $position")
        }
    }

    override fun getItemCount() = 3
}
