package com.imzqqq.app.features.roomprofile.members

import android.content.Context
import android.content.Intent
import com.airbnb.mvrx.Mavericks
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addFragment
import com.imzqqq.app.features.roomprofile.RoomProfileActivity
import com.imzqqq.app.features.roomprofile.RoomProfileArgs

class RoomMemberListActivity :
        RoomProfileActivity() {

    companion object {

        fun newIntent(context: Context, roomId: String): Intent {
            val roomProfileArgs = RoomProfileArgs(roomId)
            return Intent(context, RoomMemberListActivity::class.java).apply {
                putExtra(Mavericks.KEY_ARG, roomProfileArgs)
            }
        }
    }

    override fun addInitialFragment() {
        addFragment(R.id.simpleFragmentContainer, RoomMemberListFragment::class.java, roomProfileArgs)
    }
}
