package com.imzqqq.app.features.home.room.breadcrumbs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentBreadcrumbsBinding
import com.imzqqq.app.features.home.room.detail.RoomDetailSharedAction
import com.imzqqq.app.features.home.room.detail.RoomDetailSharedActionViewModel
import javax.inject.Inject

class BreadcrumbsFragment @Inject constructor(
        private val breadcrumbsController: BreadcrumbsController
) : VectorBaseFragment<FragmentBreadcrumbsBinding>(),
        BreadcrumbsController.Listener {

    private lateinit var sharedActionViewModel: RoomDetailSharedActionViewModel
    private val breadcrumbsViewModel: BreadcrumbsViewModel by fragmentViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBreadcrumbsBinding {
        return FragmentBreadcrumbsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        sharedActionViewModel = activityViewModelProvider.get(RoomDetailSharedActionViewModel::class.java)
    }

    override fun onDestroyView() {
        views.breadcrumbsRecyclerView.cleanup()
        breadcrumbsController.listener = null
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        views.breadcrumbsRecyclerView.configureWith(breadcrumbsController, BreadcrumbsAnimator(), hasFixedSize = false)
        breadcrumbsController.listener = this
    }

    override fun invalidate() = withState(breadcrumbsViewModel) { state ->
        breadcrumbsController.update(state)
    }

    // BreadcrumbsController.Listener **************************************************************

    override fun onBreadcrumbClicked(roomId: String) {
        sharedActionViewModel.post(RoomDetailSharedAction.SwitchToRoom(roomId))
    }

    fun scrollToTop() {
        views.breadcrumbsRecyclerView.scrollToPosition(0)
    }
}
