package com.imzqqq.app.features.roomprofile

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.viewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addFragment
import com.imzqqq.app.core.extensions.addFragmentToBackstack
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.ToolbarConfigurable
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySimpleBinding
import com.imzqqq.app.features.home.room.detail.RoomDetailPendingActionStore
import com.imzqqq.app.features.room.RequireActiveMembershipViewEvents
import com.imzqqq.app.features.room.RequireActiveMembershipViewModel
import com.imzqqq.app.features.roomprofile.alias.RoomAliasFragment
import com.imzqqq.app.features.roomprofile.banned.RoomBannedMemberListFragment
import com.imzqqq.app.features.roomprofile.members.RoomMemberListFragment
import com.imzqqq.app.features.roomprofile.notifications.RoomNotificationSettingsFragment
import com.imzqqq.app.features.roomprofile.permissions.RoomPermissionsFragment
import com.imzqqq.app.features.roomprofile.settings.RoomSettingsFragment
import com.imzqqq.app.features.roomprofile.uploads.RoomUploadsFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
open class RoomProfileActivity :
        VectorBaseActivity<ActivitySimpleBinding>(),
        ToolbarConfigurable {

    companion object {

        private const val EXTRA_DIRECT_ACCESS = "EXTRA_DIRECT_ACCESS"

        const val EXTRA_DIRECT_ACCESS_ROOM_ROOT = 0
        const val EXTRA_DIRECT_ACCESS_ROOM_SETTINGS = 1
        const val EXTRA_DIRECT_ACCESS_ROOM_MEMBERS = 2

        fun newIntent(context: Context, roomId: String, directAccess: Int?): Intent {
            val roomProfileArgs = RoomProfileArgs(roomId)
            return Intent(context, RoomProfileActivity::class.java).apply {
                putExtra(Mavericks.KEY_ARG, roomProfileArgs)
                putExtra(EXTRA_DIRECT_ACCESS, directAccess)
            }
        }
    }

    private lateinit var sharedActionViewModel: RoomProfileSharedActionViewModel
    protected lateinit var roomProfileArgs: RoomProfileArgs

    private val requireActiveMembershipViewModel: RequireActiveMembershipViewModel by viewModel()

    @Inject
    lateinit var roomDetailPendingActionStore: RoomDetailPendingActionStore

    override fun getBinding(): ActivitySimpleBinding {
        return ActivitySimpleBinding.inflate(layoutInflater)
    }

    override fun initUiAndData() {
        sharedActionViewModel = viewModelProvider.get(RoomProfileSharedActionViewModel::class.java)
        roomProfileArgs = intent?.extras?.getParcelable(Mavericks.KEY_ARG) ?: return
        if (isFirstCreation()) {
            addInitialFragment()
        }
        sharedActionViewModel
                .stream()
                .onEach { sharedAction ->
                    when (sharedAction) {
                        RoomProfileSharedAction.OpenRoomMembers                 -> openRoomMembers()
                        RoomProfileSharedAction.OpenRoomSettings                -> openRoomSettings()
                        RoomProfileSharedAction.OpenRoomAliasesSettings         -> openRoomAlias()
                        RoomProfileSharedAction.OpenRoomPermissionsSettings     -> openRoomPermissions()
                        RoomProfileSharedAction.OpenRoomUploads                 -> openRoomUploads()
                        RoomProfileSharedAction.OpenBannedRoomMembers        -> openBannedRoomMembers()
                        RoomProfileSharedAction.OpenRoomNotificationSettings -> openRoomNotificationSettings()
                    }.exhaustive
                }
                .launchIn(lifecycleScope)

        requireActiveMembershipViewModel.observeViewEvents {
            when (it) {
                is RequireActiveMembershipViewEvents.RoomLeft -> handleRoomLeft(it)
            }
        }
    }

    open fun addInitialFragment() {
        when (intent?.extras?.getInt(EXTRA_DIRECT_ACCESS, EXTRA_DIRECT_ACCESS_ROOM_ROOT)) {
            EXTRA_DIRECT_ACCESS_ROOM_SETTINGS -> {
                addFragment(R.id.simpleFragmentContainer, RoomProfileFragment::class.java, roomProfileArgs)
                addFragmentToBackstack(R.id.simpleFragmentContainer, RoomSettingsFragment::class.java, roomProfileArgs)
            }
            EXTRA_DIRECT_ACCESS_ROOM_MEMBERS -> {
                addFragment(R.id.simpleFragmentContainer, RoomMemberListFragment::class.java, roomProfileArgs)
            }
            else -> addFragment(R.id.simpleFragmentContainer, RoomProfileFragment::class.java, roomProfileArgs)
        }
    }

    override fun onResume() {
        super.onResume()
        if (roomDetailPendingActionStore.data != null) {
            finish()
        }
    }

    private fun handleRoomLeft(roomLeft: RequireActiveMembershipViewEvents.RoomLeft) {
        if (roomLeft.leftMessage != null) {
            Toast.makeText(this, roomLeft.leftMessage, Toast.LENGTH_LONG).show()
        }
        finish()
    }

    private fun openRoomUploads() {
        addFragmentToBackstack(R.id.simpleFragmentContainer, RoomUploadsFragment::class.java, roomProfileArgs)
    }

    private fun openRoomSettings() {
        addFragmentToBackstack(R.id.simpleFragmentContainer, RoomSettingsFragment::class.java, roomProfileArgs)
    }

    private fun openRoomAlias() {
        addFragmentToBackstack(R.id.simpleFragmentContainer, RoomAliasFragment::class.java, roomProfileArgs)
    }

    private fun openRoomPermissions() {
        addFragmentToBackstack(R.id.simpleFragmentContainer, RoomPermissionsFragment::class.java, roomProfileArgs)
    }

    private fun openRoomMembers() {
        addFragmentToBackstack(R.id.simpleFragmentContainer, RoomMemberListFragment::class.java, roomProfileArgs)
    }

    private fun openBannedRoomMembers() {
        addFragmentToBackstack(R.id.simpleFragmentContainer, RoomBannedMemberListFragment::class.java, roomProfileArgs)
    }

    private fun openRoomNotificationSettings() {
        addFragmentToBackstack(R.id.simpleFragmentContainer, RoomNotificationSettingsFragment::class.java, roomProfileArgs)
    }

    override fun configure(toolbar: MaterialToolbar) {
        configureToolbar(toolbar)
    }
}
