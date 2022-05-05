package com.imzqqq.app.ui.robot

import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaDialogInteractions.clickDialogNegativeButton
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.waitUntilActivityVisible
import com.imzqqq.app.espresso.tools.waitUntilViewVisible
import com.imzqqq.app.features.roommemberprofile.RoomMemberProfileActivity

class RoomSettingsRobot {

    fun crawl() {
        // Room settings
        clickListItem(R.id.matrixProfileRecyclerView, 3)
        navigateToRoomParameters()
        pressBack()

        // Notifications
        clickListItem(R.id.matrixProfileRecyclerView, 5)
        pressBack()

        assertDisplayed(R.id.roomProfileAvatarView)

        // People
        clickListItem(R.id.matrixProfileRecyclerView, 7)
        assertDisplayed(R.id.inviteUsersButton)
        navigateToRoomPeople()
        // Fab
        navigateToInvite()
        pressBack()
        pressBack()

        assertDisplayed(R.id.roomProfileAvatarView)

        // Uploads
        clickListItem(R.id.matrixProfileRecyclerView, 9)
        // File tab
        clickOn(R.string.uploads_files_title)
        waitUntilViewVisible(withText(R.string.uploads_media_title))
        pressBack()
        waitUntilViewVisible(withId(R.id.matrixProfileRecyclerView))

        assertDisplayed(R.id.roomProfileAvatarView)

        // Leave
        leaveRoom {
            negativeAction()
        }

        // Advanced
        // Room addresses

        clickListItem(R.id.matrixProfileRecyclerView, 15)
        waitUntilViewVisible(withText(R.string.room_alias_published_alias_title))
        pressBack()

        // Room permissions
        clickListItem(R.id.matrixProfileRecyclerView, 17)
        waitUntilViewVisible(withText(R.string.room_permissions_title))
        clickOn(R.string.room_permissions_change_room_avatar)
        waitUntilViewVisible(withId(android.R.id.button2))
        clickDialogNegativeButton()
        waitUntilViewVisible(withText(R.string.room_permissions_title))
        // Toggle
        clickOn(R.string.show_advanced)
        clickOn(R.string.hide_advanced)
        pressBack()

        // Menu share
        // clickMenu(R.id.roomProfileShareAction)
        // pressBack()
    }

    private fun leaveRoom(block: DialogRobot.() -> Unit) {
        clickListItem(R.id.matrixProfileRecyclerView, 13)
        waitUntilViewVisible(withId(android.R.id.button2))
        val dialogRobot = DialogRobot()
        block(dialogRobot)
        if (dialogRobot.returnedToPreviousScreen) {
            waitUntilViewVisible(withId(R.id.matrixProfileRecyclerView))
        }
    }

    private fun navigateToRoomParameters() {
        // Room history readability
        clickListItem(R.id.roomSettingsRecyclerView, 4)
        pressBack()

        // Room access
        clickListItem(R.id.roomSettingsRecyclerView, 6)
        pressBack()
    }

    private fun navigateToInvite() {
        assertDisplayed(R.id.inviteUsersButton)
        clickOn(R.id.inviteUsersButton)
        ViewActions.closeSoftKeyboard()
        pressBack()
    }

    private fun navigateToRoomPeople() {
        // Open first user
        clickListItem(R.id.roomSettingsRecyclerView, 1)
        waitUntilActivityVisible<RoomMemberProfileActivity> {
            waitUntilViewVisible(withId(R.id.memberProfilePowerLevelView))
        }

        // Verification
        clickListItem(R.id.matrixProfileRecyclerView, 1)
        waitUntilViewVisible(withId(R.id.bottomSheetRecyclerView))
        pressBack()
        waitUntilViewVisible(withId(R.id.matrixProfileRecyclerView))

        // Role
        clickListItem(R.id.matrixProfileRecyclerView, 3)
        waitUntilViewVisible(withId(android.R.id.button2))
        clickDialogNegativeButton()
        waitUntilViewVisible(withId(R.id.matrixProfileRecyclerView))
        pressBack()
    }
}
