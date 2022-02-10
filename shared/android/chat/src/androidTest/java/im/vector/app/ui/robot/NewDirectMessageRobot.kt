package com.imzqqq.app.ui.robot

import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import com.imzqqq.app.R
import com.imzqqq.app.waitForView

class NewDirectMessageRobot {

    fun verifyQrCodeButton() {
        Espresso.onView(ViewMatchers.withId(R.id.userListRecyclerView))
                .perform(waitForView(ViewMatchers.withText(R.string.qr_code)))
    }

    fun verifyInviteFriendsButton() {
        Espresso.onView(ViewMatchers.withId(R.id.userListRecyclerView))
                .perform(waitForView(ViewMatchers.withText(R.string.invite_friends)))
    }
}
