package com.imzqqq.app.features.roomprofile.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentRoomSettingGenericBinding
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.settings.VectorSettingsActivity
import org.matrix.android.sdk.api.session.room.notification.RoomNotificationState
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class RoomNotificationSettingsFragment @Inject constructor(
        val viewModelFactory: RoomNotificationSettingsViewModel.Factory,
        private val roomNotificationSettingsController: RoomNotificationSettingsController,
        private val avatarRenderer: AvatarRenderer
) : VectorBaseFragment<FragmentRoomSettingGenericBinding>(),
        RoomNotificationSettingsController.Callback {

    private val viewModel: RoomNotificationSettingsViewModel by fragmentViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRoomSettingGenericBinding {
        return FragmentRoomSettingGenericBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(views.roomSettingsToolbar)
        roomNotificationSettingsController.callback = this
        views.roomSettingsRecyclerView.configureWith(roomNotificationSettingsController, hasFixedSize = true)
        setupWaitingView()
        observeViewEvents()
    }

    override fun onDestroyView() {
        views.roomSettingsRecyclerView.cleanup()
        roomNotificationSettingsController.callback = null
        super.onDestroyView()
    }

    private fun setupWaitingView() {
        views.waitingView.waitingStatusText.setText(R.string.please_wait)
        views.waitingView.waitingStatusText.isVisible = true
    }

    private fun observeViewEvents() {
        viewModel.observeViewEvents {
            when (it) {
                is RoomNotificationSettingsViewEvents.Failure -> displayErrorDialog(it.throwable)
            }
        }
    }

    override fun invalidate() = withState(viewModel) { viewState ->
        roomNotificationSettingsController.setData(viewState)
        views.waitingView.root.isVisible = viewState.isLoading
        renderRoomSummary(viewState)
    }

    override fun didSelectRoomNotificationState(roomNotificationState: RoomNotificationState) {
        viewModel.handle(RoomNotificationSettingsAction.SelectNotificationState(roomNotificationState))
    }

    override fun didSelectAccountSettingsLink() {
        navigator.openSettings(requireContext(), VectorSettingsActivity.EXTRA_DIRECT_ACCESS_NOTIFICATIONS)
    }

    private fun renderRoomSummary(state: RoomNotificationSettingsViewState) {
        state.roomSummary()?.let {
            views.roomSettingsToolbarTitleView.text = it.displayName
            avatarRenderer.render(it.toMatrixItem(), views.roomSettingsToolbarAvatarImageView)
            views.roomSettingsDecorationToolbarAvatarImageView.render(it.roomEncryptionTrustLevel)
        }
    }
}
