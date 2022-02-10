package com.imzqqq.app.flow.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.imzqqq.app.flow.TabData
import com.imzqqq.app.flow.util.CustomFragmentStateAdapter

class FlowPagerAdapter(
    val tabs: List<TabData>, activity: FragmentActivity
) : CustomFragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        val tab = tabs[position]
        return tab.fragment(tab.arguments)
    }

    override fun getItemCount() = tabs.size
}
