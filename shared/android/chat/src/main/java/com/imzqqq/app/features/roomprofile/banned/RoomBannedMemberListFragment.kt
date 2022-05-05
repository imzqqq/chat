package com.imzqqq.app.features.roomprofile.banned

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.toast
import com.imzqqq.app.databinding.FragmentRoomSettingGenericBinding
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.roomprofile.RoomProfileArgs
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class RoomBannedMemberListFragment @Inject constructor(
        private val roomMemberListController: RoomBannedMemberListController,
        private val avatarRenderer: AvatarRenderer
) : VectorBaseFragment<FragmentRoomSettingGenericBinding>(),
        RoomBannedMemberListController.Callback {

    private val viewModel: RoomBannedMemberListViewModel by fragmentViewModel()
    private val roomProfileArgs: RoomProfileArgs by args()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRoomSettingGenericBinding {
        return FragmentRoomSettingGenericBinding.inflate(inflater, container, false)
    }

    override fun onUnbanClicked(roomMember: RoomMemberSummary) {
        viewModel.handle(RoomBannedMemberListAction.QueryInfo(roomMember))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomMemberListController.callback = this
        setupToolbar(views.roomSettingsToolbar)
        setupSearchView()
        views.roomSettingsRecyclerView.configureWith(roomMemberListController, hasFixedSize = true)

        viewModel.observeViewEvents {
            when (it) {
                is RoomBannedMemberListViewEvents.ShowBannedInfo -> {
                    val canBan = withState(viewModel) { state -> state.canUserBan }
                    MaterialAlertDialogBuilder(requireActivity())
                            .setTitle(getString(R.string.member_banned_by, it.bannedByUserId))
                            .setMessage(getString(R.string.reason_colon, it.banReason))
                            .setPositiveButton(R.string.ok, null)
                            .apply {
                                if (canBan) {
                                    setNegativeButton(R.string.room_participants_action_unban) { _, _ ->
                                        viewModel.handle(RoomBannedMemberListAction.UnBanUser(it.roomMemberSummary))
                                    }
                                }
                            }
                            .show()
                }
                is RoomBannedMemberListViewEvents.ToastError     -> {
                    requireActivity().toast(it.info)
                }
            }
        }
    }

    override fun onDestroyView() {
        views.roomSettingsRecyclerView.cleanup()
        super.onDestroyView()
    }

    private fun setupSearchView() {
        views.searchViewAppBarLayout.isVisible = true
        views.searchView.queryHint = getString(R.string.search_banned_user_hint)
        views.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.handle(RoomBannedMemberListAction.Filter(newText))
                return true
            }
        })
    }

    override fun invalidate() = withState(viewModel) { viewState ->
        roomMemberListController.setData(viewState)
        renderRoomSummary(viewState)
    }

    private fun renderRoomSummary(state: RoomBannedMemberListViewState) {
        state.roomSummary()?.let {
            views.roomSettingsToolbarTitleView.text = it.displayName
            avatarRenderer.render(it.toMatrixItem(), views.roomSettingsToolbarAvatarImageView)
            views.roomSettingsDecorationToolbarAvatarImageView.render(it.roomEncryptionTrustLevel)
        }
    }
}
