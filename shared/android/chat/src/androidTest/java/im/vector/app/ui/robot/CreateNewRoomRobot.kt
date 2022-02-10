package com.imzqqq.app.ui.robot

import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.adevinta.android.barista.assertion.BaristaListAssertions
import com.adevinta.android.barista.interaction.BaristaClickInteractions
import com.adevinta.android.barista.interaction.BaristaListInteractions
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.waitUntilActivityVisible
import com.imzqqq.app.espresso.tools.waitUntilViewVisible
import com.imzqqq.app.features.home.room.detail.RoomDetailActivity

class CreateNewRoomRobot(
        var createdRoom: Boolean = false
) {

    fun createRoom(block: RoomDetailRobot.() -> Unit) {
        createdRoom = true
        BaristaListAssertions.assertListItemCount(R.id.createRoomForm, 12)
        BaristaListInteractions.clickListItemChild(R.id.createRoomForm, 11, R.id.form_submit_button)
        waitUntilActivityVisible<RoomDetailActivity> {
            waitUntilViewVisible(withId(R.id.composerEditText))
        }
        block(RoomDetailRobot())
        pressBack()
    }

    fun crawl() {
        // Room access bottom sheet
        BaristaClickInteractions.clickOn(R.string.room_settings_room_access_private_title)
        pressBack()
    }
}
