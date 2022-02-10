package com.imzqqq.app.features.roomdirectory.roompreview

import android.graphics.Typeface
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.core.platform.ButtonStateView
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.styleMatchingText
import com.imzqqq.app.core.utils.tappableMatchingText
import com.imzqqq.app.databinding.FragmentRoomPreviewNoPreviewBinding
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.navigation.Navigator
import com.imzqqq.app.features.roomdirectory.JoinState
import com.imzqqq.app.features.settings.VectorSettingsActivity
import com.imzqqq.app.features.themes.ThemeUtils
import me.gujun.android.span.span
import org.matrix.android.sdk.api.session.room.model.RoomType
import org.matrix.android.sdk.api.util.MatrixItem
import javax.inject.Inject

/**
 * Note: this Fragment is also used for world readable room for the moment
 */
class RoomPreviewNoPreviewFragment @Inject constructor(
        private val avatarRenderer: AvatarRenderer
) : VectorBaseFragment<FragmentRoomPreviewNoPreviewBinding>() {

    private val roomPreviewViewModel: RoomPreviewViewModel by fragmentViewModel()
    private val roomPreviewData: RoomPreviewData by args()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRoomPreviewNoPreviewBinding {
        return FragmentRoomPreviewNoPreviewBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(views.roomPreviewNoPreviewToolbar)

        views.roomPreviewNoPreviewJoin.commonClicked = { roomPreviewViewModel.handle(RoomPreviewAction.Join) }
    }

    override fun invalidate() = withState(roomPreviewViewModel) { state ->

        views.roomPreviewNoPreviewJoin.render(
                when (state.roomJoinState) {
                    JoinState.NOT_JOINED    -> ButtonStateView.State.Button
                    JoinState.JOINING       -> ButtonStateView.State.Loading
                    JoinState.JOINED        -> ButtonStateView.State.Loaded
                    JoinState.JOINING_ERROR -> ButtonStateView.State.Error
                }
        )

        if (state.lastError == null) {
            views.roomPreviewNoPreviewError.isVisible = false
        } else {
            views.roomPreviewNoPreviewError.isVisible = true
            views.roomPreviewNoPreviewError.text = errorFormatter.toHumanReadable(state.lastError)
        }

        if (state.roomJoinState == JoinState.JOINED) {
            // Quit this screen
            requireActivity().finish()
            // Open room
            if (state.roomType == RoomType.SPACE) {
                navigator.switchToSpace(requireActivity(), state.roomId, Navigator.PostSwitchSpaceAction.None)
            } else {
                navigator.openRoom(requireActivity(), state.roomId, roomPreviewData.eventId, roomPreviewData.buildTask)
            }
        }

        val bestName = state.roomName ?: state.roomAlias ?: state.roomId
        when (state.peekingState) {
            is Loading -> {
                views.roomPreviewPeekingProgress.isVisible = true
                views.roomPreviewNoPreviewJoin.isVisible = false
            }
            is Success -> {
                views.roomPreviewPeekingProgress.isVisible = false
                when (state.peekingState.invoke()) {
                    PeekingState.FOUND     -> {
                        // show join buttons
                        views.roomPreviewNoPreviewJoin.isVisible = true
                        renderState(bestName, state.matrixItem(), state.roomTopic
                                /**, state.roomType*/)
                        if (state.fromEmailInvite != null && !state.isEmailBoundToAccount) {
                            views.roomPreviewNoPreviewLabel.text =
                                    span {
                                        span {
                                            textColor = ThemeUtils.getColor(requireContext(), R.attr.vctr_content_primary)
                                            text = if (state.roomType == RoomType.SPACE) {
                                                getString(R.string.this_invite_to_this_space_was_sent, state.fromEmailInvite.email)
                                            } else {
                                                getString(R.string.this_invite_to_this_room_was_sent, state.fromEmailInvite.email)
                                            }
                                                    .toSpannable()
                                                    .styleMatchingText(state.fromEmailInvite.email, Typeface.BOLD)
                                        }
                                        +"\n"
                                        span {
                                            text = getString(
                                                    R.string.link_this_email_with_your_account,
                                                    getString(R.string.link_this_email_settings_link)
                                            )
                                                    .toSpannable()
                                                    .tappableMatchingText(getString(R.string.link_this_email_settings_link), object : ClickableSpan() {
                                                        override fun onClick(widget: View) {
                                                            navigator.openSettings(
                                                                    requireContext(),
                                                                    VectorSettingsActivity.EXTRA_DIRECT_ACCESS_DISCOVERY_SETTINGS
                                                            )
                                                        }
                                                    })
                                        }
                                    }
                            views.roomPreviewNoPreviewLabel.movementMethod = LinkMovementMethod.getInstance()
                            views.roomPreviewNoPreviewJoin.commonClicked = {
                                roomPreviewViewModel.handle(RoomPreviewAction.JoinThirdParty)
                            }
                        }
                    }
                    PeekingState.NO_ACCESS -> {
                        views.roomPreviewNoPreviewJoin.isVisible = true
                        views.roomPreviewNoPreviewLabel.isVisible = true
                        views.roomPreviewNoPreviewLabel.setText(R.string.room_preview_no_preview_join)
                        renderState(bestName, state.matrixItem().takeIf { state.roomAlias != null }, state.roomTopic
                                /**, state.roomType*/)
                    }
                    else                   -> {
                        views.roomPreviewNoPreviewJoin.isVisible = false
                        views.roomPreviewNoPreviewLabel.isVisible = true
                        views.roomPreviewNoPreviewLabel.setText(R.string.room_preview_not_found)
                        renderState(bestName, null, state.roomTopic
                                /**, state.roomType*/)
                    }
                }
            }
            else       -> {
                // Render with initial state, no peeking
                views.roomPreviewPeekingProgress.isVisible = false
                views.roomPreviewNoPreviewJoin.isVisible = true
                renderState(bestName, state.matrixItem(), state.roomTopic
                        /**, state.roomType*/)
                views.roomPreviewNoPreviewLabel.isVisible = false
            }
        }
    }

    private fun renderState(roomName: String, matrixItem: MatrixItem?, topic: String?
            /**, roomType: String?*/
    ) {
        // Toolbar
        if (matrixItem != null) {
            views.roomPreviewNoPreviewToolbarAvatar.isVisible = true
            views.roomPreviewNoPreviewAvatar.isVisible = true
            avatarRenderer.render(matrixItem, views.roomPreviewNoPreviewToolbarAvatar)
            avatarRenderer.render(matrixItem, views.roomPreviewNoPreviewAvatar)
        } else {
            views.roomPreviewNoPreviewToolbarAvatar.isVisible = false
            views.roomPreviewNoPreviewAvatar.isVisible = false
        }
        views.roomPreviewNoPreviewToolbarTitle.text = roomName

        // Screen
        views.roomPreviewNoPreviewName.text = roomName
        views.roomPreviewNoPreviewTopic.setTextOrHide(topic)
    }
}
