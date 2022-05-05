package com.imzqqq.app.features.roomprofile.uploads.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.trackItemsVisibilityChange
import com.imzqqq.app.core.platform.StateView
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentGenericStateViewRecyclerBinding
import com.imzqqq.app.features.roomprofile.uploads.RoomUploadsAction
import com.imzqqq.app.features.roomprofile.uploads.RoomUploadsViewModel
import org.matrix.android.sdk.api.session.room.uploads.UploadEvent
import javax.inject.Inject

class RoomUploadsFilesFragment @Inject constructor(
        private val controller: UploadsFileController
) : VectorBaseFragment<FragmentGenericStateViewRecyclerBinding>(),
        UploadsFileController.Listener,
        StateView.EventCallback {

    private val uploadsViewModel by parentFragmentViewModel(RoomUploadsViewModel::class)

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenericStateViewRecyclerBinding {
        return FragmentGenericStateViewRecyclerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.genericStateViewListStateView.contentView = views.genericStateViewListRecycler
        views.genericStateViewListStateView.eventCallback = this

        views.genericStateViewListRecycler.trackItemsVisibilityChange()
        views.genericStateViewListRecycler.configureWith(controller, dividerDrawable = R.drawable.divider_horizontal)
        controller.listener = this
    }

    override fun onDestroyView() {
        views.genericStateViewListRecycler.cleanup()
        controller.listener = null
        super.onDestroyView()
    }

    override fun onOpenClicked(uploadEvent: UploadEvent) {
        // Same action than Share
        uploadsViewModel.handle(RoomUploadsAction.Share(uploadEvent))
    }

    override fun onRetryClicked() {
        uploadsViewModel.handle(RoomUploadsAction.Retry)
    }

    override fun loadMore() {
        uploadsViewModel.handle(RoomUploadsAction.LoadMore)
    }

    override fun onDownloadClicked(uploadEvent: UploadEvent) {
        uploadsViewModel.handle(RoomUploadsAction.Download(uploadEvent))
    }

    override fun onShareClicked(uploadEvent: UploadEvent) {
        uploadsViewModel.handle(RoomUploadsAction.Share(uploadEvent))
    }

    override fun invalidate() = withState(uploadsViewModel) { state ->
        if (state.fileEvents.isEmpty()) {
            when (state.asyncEventsRequest) {
                is Loading -> {
                    views.genericStateViewListStateView.state = StateView.State.Loading
                }
                is Fail    -> {
                    views.genericStateViewListStateView.state = StateView.State.Error(errorFormatter.toHumanReadable(state.asyncEventsRequest.error))
                }
                is Success -> {
                    if (state.hasMore) {
                        // We need to load more items
                        loadMore()
                    } else {
                        views.genericStateViewListStateView.state = StateView.State.Empty(
                                title = getString(R.string.uploads_files_no_result),
                                image = ContextCompat.getDrawable(requireContext(), R.drawable.ic_file)
                        )
                    }
                }
            }
        } else {
            views.genericStateViewListStateView.state = StateView.State.Content
            controller.setData(state)
        }
    }
}
