/* Copyright 2019 Flow Contributors
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

package com.keylesspalace.flow.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.keylesspalace.flow.components.timeline.TimelineFragment
import com.keylesspalace.flow.components.timeline.TimelineViewModel
import com.keylesspalace.flow.fragment.AccountMediaFragment
import com.keylesspalace.flow.interfaces.RefreshableFragment
import com.keylesspalace.flow.util.CustomFragmentStateAdapter

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
