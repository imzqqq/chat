package com.imzqqq.app.flow.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.imzqqq.app.flow.components.timeline.TimelineFragment
import com.imzqqq.app.flow.components.timeline.TimelineViewModel
import com.imzqqq.app.flow.fragment.AccountMediaFragment
import com.imzqqq.app.flow.interfaces.RefreshableFragment
import com.imzqqq.app.flow.util.CustomFragmentStateAdapter

class AccountPagerAdapter(
    activity: FragmentActivity,
    private val accountId: String
) : CustomFragmentStateAdapter(activity) {

    override fun getItemCount() = TAB_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TimelineFragment.newInstance(TimelineViewModel.Kind.USER, accountId, false)
            1 -> TimelineFragment.newInstance(TimelineViewModel.Kind.USER_WITH_REPLIES, accountId, false)
            2 -> TimelineFragment.newInstance(TimelineViewModel.Kind.USER_PINNED, accountId, false)
            3 -> AccountMediaFragment.newInstance(accountId, false)
            else -> throw AssertionError("Page $position is out of AccountPagerAdapter bounds")
        }
    }

    fun refreshContent() {
        for (i in 0 until TAB_COUNT) {
            val fragment = getFragment(i)
            if (fragment != null && fragment is RefreshableFragment) {
                (fragment as RefreshableFragment).refreshContent()
            }
        }
    }

    companion object {
        private const val TAB_COUNT = 4
    }
}
