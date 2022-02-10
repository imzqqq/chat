package com.imzqqq.app.features.roomprofile.uploads

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.imzqqq.app.features.roomprofile.uploads.files.RoomUploadsFilesFragment
import com.imzqqq.app.features.roomprofile.uploads.media.RoomUploadsMediaFragment

class RoomUploadsPagerAdapter(
        private val fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            fragment.childFragmentManager.fragmentFactory.instantiate(fragment.requireContext().classLoader, RoomUploadsMediaFragment::class.java.name)
        } else {
            fragment.childFragmentManager.fragmentFactory.instantiate(fragment.requireContext().classLoader, RoomUploadsFilesFragment::class.java.name)
        }
    }
}
