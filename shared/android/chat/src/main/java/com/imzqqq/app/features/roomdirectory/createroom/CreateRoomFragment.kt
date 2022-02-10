package com.imzqqq.app.features.roomdirectory.createroom

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.dialogs.GalleryOrCameraDialogHelper
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.OnBackPressed
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.databinding.FragmentCreateRoomBinding
import com.imzqqq.app.features.navigation.Navigator
import com.imzqqq.app.features.roomdirectory.RoomDirectorySharedAction
import com.imzqqq.app.features.roomdirectory.RoomDirectorySharedActionViewModel
import com.imzqqq.app.features.roomprofile.settings.joinrule.RoomJoinRuleBottomSheet
import com.imzqqq.app.features.roomprofile.settings.joinrule.RoomJoinRuleSharedActionViewModel
import com.imzqqq.app.features.roomprofile.settings.joinrule.toOption
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.session.room.failure.CreateRoomFailure
import org.matrix.android.sdk.api.session.room.model.RoomJoinRules
import javax.inject.Inject

@Parcelize
data class CreateRoomArgs(
        val initialName: String,
        val parentSpaceId: String? = null,
        val isSpace: Boolean = false
) : Parcelable

class CreateRoomFragment @Inject constructor(
        private val createRoomController: CreateRoomController,
        private val createSpaceController: CreateSubSpaceController,
        colorProvider: ColorProvider
) : VectorBaseFragment<FragmentCreateRoomBinding>(),
        CreateRoomController.Listener,
        GalleryOrCameraDialogHelper.Listener,
        OnBackPressed {

    private lateinit var sharedActionViewModel: RoomDirectorySharedActionViewModel
    private val viewModel: CreateRoomViewModel by fragmentViewModel()
    private val args: CreateRoomArgs by args()

    private lateinit var roomJoinRuleSharedActionViewModel: RoomJoinRuleSharedActionViewModel

    private val galleryOrCameraDialogHelper = GalleryOrCameraDialogHelper(this, colorProvider)

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCreateRoomBinding {
        return FragmentCreateRoomBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vectorBaseActivity.setSupportActionBar(views.createRoomToolbar)
        sharedActionViewModel = activityViewModelProvider.get(RoomDirectorySharedActionViewModel::class.java)
        setupRoomJoinRuleSharedActionViewModel()
        setupWaitingView()
        setupRecyclerView()
        views.createRoomClose.debouncedClicks {
            sharedActionViewModel.post(RoomDirectorySharedAction.Back)
        }
        viewModel.observeViewEvents {
            when (it) {
                CreateRoomViewEvents.Quit       -> vectorBaseActivity.onBackPressed()
                is CreateRoomViewEvents.Failure -> showFailure(it.throwable)
            }.exhaustive
        }
    }

    override fun onResume() {
        super.onResume()
        views.createRoomTitle.text = getString(if (args.isSpace) R.string.create_new_space else R.string.create_new_room)
    }

    private fun setupRoomJoinRuleSharedActionViewModel() {
        roomJoinRuleSharedActionViewModel = activityViewModelProvider.get(RoomJoinRuleSharedActionViewModel::class.java)
        roomJoinRuleSharedActionViewModel
                .stream()
                .onEach { action ->
                    viewModel.handle(CreateRoomAction.SetVisibility(action.roomJoinRule))
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun showFailure(throwable: Throwable) {
        // Note: RoomAliasError are displayed directly in the form
        if (throwable !is CreateRoomFailure.AliasError) {
            super.showFailure(throwable)
        }
    }

    private fun setupWaitingView() {
        views.waitingView.waitingStatusText.isVisible = true
        views.waitingView.waitingStatusText.setText(
                if (args.isSpace) R.string.create_space_in_progress else R.string.create_room_in_progress
        )
    }

    override fun onDestroyView() {
        views.createRoomForm.cleanup()
        createRoomController.listener = null
        createSpaceController.listener = null
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        if (args.isSpace) {
            views.createRoomForm.configureWith(createSpaceController)
            createSpaceController.listener = this
        } else {
            views.createRoomForm.configureWith(createRoomController)
            createRoomController.listener = this
        }
    }

    override fun onAvatarDelete() {
        viewModel.handle(CreateRoomAction.SetAvatar(null))
    }

    override fun onAvatarChange() {
        galleryOrCameraDialogHelper.show()
    }

    override fun onImageReady(uri: Uri?) {
        viewModel.handle(CreateRoomAction.SetAvatar(uri))
    }

    override fun onNameChange(newName: String) {
        viewModel.handle(CreateRoomAction.SetName(newName))
    }

    override fun onTopicChange(newTopic: String) {
        viewModel.handle(CreateRoomAction.SetTopic(newTopic))
    }

    override fun selectVisibility() = withState(viewModel) { state ->
        // If restricted is supported and the user is in the context of a parent space
        // then show restricted option.
        val allowed = if (state.supportsRestricted && state.parentSpaceId != null) {
            listOf(RoomJoinRules.INVITE, RoomJoinRules.PUBLIC, RoomJoinRules.RESTRICTED)
        } else {
            listOf(RoomJoinRules.INVITE, RoomJoinRules.PUBLIC)
        }
        RoomJoinRuleBottomSheet.newInstance(state.roomJoinRules,
                allowed.map { it.toOption(false) },
                state.isSubSpace,
                state.parentSpaceSummary?.displayName
        )
                .show(childFragmentManager, "RoomJoinRuleBottomSheet")
    }
//    override fun setIsPublic(isPublic: Boolean) {
//        viewModel.handle(CreateRoomAction.SetIsPublic(isPublic))
//    }

    override fun setAliasLocalPart(aliasLocalPart: String) {
        viewModel.handle(CreateRoomAction.SetRoomAliasLocalPart(aliasLocalPart))
    }

    override fun setIsEncrypted(isEncrypted: Boolean) {
        viewModel.handle(CreateRoomAction.SetIsEncrypted(isEncrypted))
    }

    override fun toggleShowAdvanced() {
        viewModel.handle(CreateRoomAction.ToggleShowAdvanced)
    }

    override fun setDisableFederation(disableFederation: Boolean) {
        viewModel.handle(CreateRoomAction.DisableFederation(disableFederation))
    }

    override fun submit() {
        viewModel.handle(CreateRoomAction.Create)
    }

    override fun onBackPressed(toolbarButton: Boolean): Boolean {
        return withState(viewModel) {
            return@withState if (!it.isEmpty()) {
                MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.dialog_title_warning)
                        .setMessage(R.string.warning_room_not_created_yet)
                        .setPositiveButton(R.string.yes) { _, _ ->
                            viewModel.handle(CreateRoomAction.Reset)
                        }
                        .setNegativeButton(R.string.no, null)
                        .show()
                true
            } else {
                false
            }
        }
    }

    override fun invalidate() = withState(viewModel) { state ->
        val async = state.asyncCreateRoomRequest
        views.waitingView.root.isVisible = async is Loading
        if (async is Success) {
            // Navigate to freshly created room
            if (state.isSubSpace) {
                navigator.switchToSpace(
                        requireContext(),
                        async(),
                        Navigator.PostSwitchSpaceAction.None
                )
            } else {
                navigator.openRoom(requireActivity(), async())
            }

            sharedActionViewModel.post(RoomDirectorySharedAction.Close)
        } else {
            // Populate list with Epoxy
            if (args.isSpace) {
                createSpaceController.setData(state)
            } else {
                createRoomController.setData(state)
            }
        }
    }
}
