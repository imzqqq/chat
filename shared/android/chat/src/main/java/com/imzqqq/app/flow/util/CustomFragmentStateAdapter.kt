package com.imzqqq.app.flow.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class CustomFragmentStateAdapter(
    private val activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    fun getFragment(position: Int): Fragment? =
        activity.supportFragmentManager.findFragmentByTag("f" + getItemId(position))
}
