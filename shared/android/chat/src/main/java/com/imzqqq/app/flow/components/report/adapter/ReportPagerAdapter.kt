package com.imzqqq.app.flow.components.report.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.imzqqq.app.flow.components.report.fragments.ReportDoneFragment
import com.imzqqq.app.flow.components.report.fragments.ReportNoteFragment
import com.imzqqq.app.flow.components.report.fragments.ReportStatusesFragment

class ReportPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReportStatusesFragment.newInstance()
            1 -> ReportNoteFragment.newInstance()
            2 -> ReportDoneFragment.newInstance()
            else -> throw IllegalArgumentException("Unknown page index: $position")
        }
    }

    override fun getItemCount() = 3
}
