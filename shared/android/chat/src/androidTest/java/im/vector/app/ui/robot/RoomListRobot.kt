package com.imzqqq.app.ui.robot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.waitUntilActivityVisible
import com.imzqqq.app.features.roomdirectory.RoomDirectoryActivity

class RoomListRobot {

    fun openRoom(roomName: String, block: RoomDetailRobot.() -> Unit) {
        clickOn(roomName)
        block(RoomDetailRobot())
        pressBack()
    }

    fun verifyCreatedRoom() {
        onView(ViewMatchers.withId(R.id.roomListView))
                .perform(
                        RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                                ViewMatchers.hasDescendant(withText(R.string.room_displayname_empty_room)),
                                ViewActions.longClick()
                        )
                )
        pressBack()
    }

    fun newRoom(block: NewRoomRobot.() -> Unit) {
        clickOn(R.id.createGroupRoomButton)
        waitUntilActivityVisible<RoomDirectoryActivity> {
            BaristaVisibilityAssertions.assertDisplayed(R.id.publicRoomsList)
        }
        val newRoomRobot = NewRoomRobot()
        block(newRoomRobot)
        if (!newRoomRobot.createdRoom) {
            pressBack()
        }
    }
}
