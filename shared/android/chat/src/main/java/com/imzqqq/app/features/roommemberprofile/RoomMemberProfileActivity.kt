package com.imzqqq.app.features.roommemberprofile

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.viewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addFragment
import com.imzqqq.app.core.platform.ToolbarConfigurable
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySimpleBinding
import com.imzqqq.app.features.room.RequireActiveMembershipViewEvents
import com.imzqqq.app.features.room.RequireActiveMembershipViewModel

@AndroidEntryPoint
class RoomMemberProfileActivity :
        VectorBaseActivity<ActivitySimpleBinding>(),
        ToolbarConfigurable {

    companion object {
        fun newIntent(context: Context, args: RoomMemberProfileArgs): Intent {
            return Intent(context, RoomMemberProfileActivity::class.java).apply {
                putExtra(Mavericks.KEY_ARG, args)
            }
        }
    }

    private val requireActiveMembershipViewModel: RequireActiveMembershipViewModel by viewModel()

    override fun getBinding(): ActivitySimpleBinding {
        return ActivitySimpleBinding.inflate(layoutInflater)
    }

    override fun initUiAndData() {
        if (isFirstCreation()) {
            val fragmentArgs: RoomMemberProfileArgs = intent?.extras?.getParcelable(Mavericks.KEY_ARG) ?: return
            addFragment(R.id.simpleFragmentContainer, RoomMemberProfileFragment::class.java, fragmentArgs)
        }

        requireActiveMembershipViewModel.observeViewEvents {
            when (it) {
                is RequireActiveMembershipViewEvents.RoomLeft -> handleRoomLeft(it)
            }
        }
    }

    override fun configure(toolbar: MaterialToolbar) {
        configureToolbar(toolbar)
    }

    private fun handleRoomLeft(roomLeft: RequireActiveMembershipViewEvents.RoomLeft) {
        if (roomLeft.leftMessage != null) {
            Toast.makeText(this, roomLeft.leftMessage, Toast.LENGTH_LONG).show()
        }
        finish()
    }
}
