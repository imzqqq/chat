package com.imzqqq.app.features.spaces.explore

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.registerStartForActivityResult
import com.imzqqq.app.core.platform.OnBackPressed
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.utils.colorizeMatchingText
import com.imzqqq.app.core.utils.isValidUrl
import com.imzqqq.app.core.utils.openUrlInExternalBrowser
import com.imzqqq.app.databinding.FragmentSpaceDirectoryBinding
import com.imzqqq.app.features.home.room.detail.timeline.TimelineEventController
import com.imzqqq.app.features.matrixto.SpaceCardRenderer
import com.imzqqq.app.features.permalink.PermalinkHandler
import com.imzqqq.app.features.spaces.manage.ManageType
import com.imzqqq.app.features.spaces.manage.SpaceAddRoomSpaceChooserBottomSheet
import com.imzqqq.app.features.spaces.manage.SpaceManageActivity
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.session.room.model.SpaceChildInfo
import java.net.URL
import javax.inject.Inject

@Parcelize
data class SpaceDirectoryArgs(
        val spaceId: String
) : Parcelable

class SpaceDirectoryFragment @Inject constructor(
        private val epoxyController: SpaceDirectoryController,
        private val permalinkHandler: PermalinkHandler,
        private val spaceCardRenderer: SpaceCardRenderer,
        private val colorProvider: ColorProvider
) : VectorBaseFragment<FragmentSpaceDirectoryBinding>(),
        SpaceDirectoryController.InteractionListener,
        TimelineEventController.UrlClickCallback,
        OnBackPressed {

    override fun getMenuRes() = R.menu.menu_space_directory

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            FragmentSpaceDirectoryBinding.inflate(layoutInflater, container, false)

    private val viewModel by activityViewModel(SpaceDirectoryViewModel::class)
    private val epoxyVisibilityTracker = EpoxyVisibilityTracker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.setFragmentResultListener(SpaceAddRoomSpaceChooserBottomSheet.REQUEST_KEY, this) { _, bundle ->

            bundle.getString(SpaceAddRoomSpaceChooserBottomSheet.BUNDLE_KEY_ACTION)?.let { action ->
                val spaceId = withState(viewModel) { it.spaceId }
                when (action) {
                    SpaceAddRoomSpaceChooserBottomSheet.ACTION_ADD_ROOMS  -> {
                        addExistingRoomActivityResult.launch(SpaceManageActivity.newIntent(requireContext(), spaceId, ManageType.AddRooms))
                    }
                    SpaceAddRoomSpaceChooserBottomSheet.ACTION_ADD_SPACES -> {
                        addExistingRoomActivityResult.launch(SpaceManageActivity.newIntent(requireContext(), spaceId, ManageType.AddRoomsOnlySpaces))
                    }
                    else                                                  -> {
                        // nop
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vectorBaseActivity.setSupportActionBar(views.toolbar)

        vectorBaseActivity.supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
        epoxyController.listener = this
        views.spaceDirectoryList.configureWith(epoxyController)
        epoxyVisibilityTracker.attach(views.spaceDirectoryList)

        viewModel.onEach(SpaceDirectoryState::canAddRooms) {
            invalidateOptionsMenu()
        }

        views.spaceCard.matrixToCardMainButton.isVisible = false
        views.spaceCard.matrixToCardSecondaryButton.isVisible = false
    }

    override fun onDestroyView() {
        epoxyController.listener = null
        epoxyVisibilityTracker.detach(views.spaceDirectoryList)
        views.spaceDirectoryList.cleanup()
        super.onDestroyView()
    }

    override fun invalidate() = withState(viewModel) { state ->
        epoxyController.setData(state)

        val currentParentId = state.hierarchyStack.lastOrNull()

        if (currentParentId == null) {
            // it's the root
            val title = getString(R.string.space_explore_activity_title)
            views.toolbar.title = title
        } else {
            val title = state.currentRootSummary?.name
                    ?: state.currentRootSummary?.canonicalAlias
                    ?: getString(R.string.space_explore_activity_title)
            views.toolbar.title = title
        }

        spaceCardRenderer.render(state.currentRootSummary, emptyList(), this, views.spaceCard)
    }

    override fun onPrepareOptionsMenu(menu: Menu) = withState(viewModel) { state ->
        menu.findItem(R.id.spaceAddRoom)?.isVisible = state.canAddRooms
        menu.findItem(R.id.spaceCreateRoom)?.isVisible = false // Not yet implemented
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.spaceAddRoom    -> {
                withState(viewModel) { state ->
                    addExistingRooms(state.spaceId)
                }
                return true
            }
            R.id.spaceCreateRoom -> {
                // not implemented yet
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onButtonClick(spaceChildInfo: SpaceChildInfo) {
        viewModel.handle(SpaceDirectoryViewAction.JoinOrOpen(spaceChildInfo))
    }

    override fun onSpaceChildClick(spaceChildInfo: SpaceChildInfo) {
        viewModel.handle(SpaceDirectoryViewAction.ExploreSubSpace(spaceChildInfo))
    }

    override fun onRoomClick(spaceChildInfo: SpaceChildInfo) {
        viewModel.handle(SpaceDirectoryViewAction.ShowDetails(spaceChildInfo))
    }

    override fun onBackPressed(toolbarButton: Boolean): Boolean {
        viewModel.handle(SpaceDirectoryViewAction.HandleBack)
        return true
    }

    override fun retry() {
        viewModel.handle(SpaceDirectoryViewAction.Retry)
    }

    private val addExistingRoomActivityResult = registerStartForActivityResult { _ ->
        viewModel.handle(SpaceDirectoryViewAction.Retry)
    }

    override fun addExistingRooms(spaceId: String) {
        SpaceAddRoomSpaceChooserBottomSheet.newInstance().show(childFragmentManager, "SpaceAddRoomSpaceChooserBottomSheet")
    }

    override fun loadAdditionalItemsIfNeeded() {
        viewModel.handle(SpaceDirectoryViewAction.LoadAdditionalItemsIfNeeded)
    }

    override fun onUrlClicked(url: String, title: String): Boolean {
        viewLifecycleOwner.lifecycleScope.launch {
            val isHandled = permalinkHandler.launch(requireActivity(), url, null)
            if (!isHandled) {
                if (title.isValidUrl() && url.isValidUrl() && URL(title).host != URL(url).host) {
                    MaterialAlertDialogBuilder(requireActivity(), R.style.ThemeOverlay_Vector_MaterialAlertDialog_Destructive)
                            .setTitle(R.string.external_link_confirmation_title)
                            .setMessage(
                                    getString(R.string.external_link_confirmation_message, title, url)
                                            .toSpannable()
                                            .colorizeMatchingText(url, colorProvider.getColorFromAttribute(R.attr.vctr_content_tertiary))
                                            .colorizeMatchingText(title, colorProvider.getColorFromAttribute(R.attr.vctr_content_tertiary))
                            )
                            .setPositiveButton(R.string._continue) { _, _ ->
                                openUrlInExternalBrowser(requireContext(), url)
                            }
                            .setNegativeButton(R.string.cancel, null)
                            .show()
                } else {
                    // Open in external browser, in a new Tab
                    openUrlInExternalBrowser(requireContext(), url)
                }
            }
        }
        // In fact it is always managed
        return true
    }

    override fun onUrlLongClicked(url: String): Boolean {
        // nothing?
        return false
    }
}
