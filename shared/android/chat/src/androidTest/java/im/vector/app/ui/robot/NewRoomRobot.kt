package com.imzqqq.app.ui.robot

import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.waitUntilViewVisible

class NewRoomRobot(
        var createdRoom: Boolean = false
) {

    fun createNewRoom(block: CreateNewRoomRobot.() -> Unit) {
        clickOn(R.string.create_new_room)
        waitUntilViewVisible(withId(R.id.createRoomForm))
        val createNewRoomRobot = CreateNewRoomRobot()
        block(createNewRoomRobot)
        createdRoom = createNewRoomRobot.createdRoom
        if (!createNewRoomRobot.createdRoom) {
            pressBack()
        }
    }
}
