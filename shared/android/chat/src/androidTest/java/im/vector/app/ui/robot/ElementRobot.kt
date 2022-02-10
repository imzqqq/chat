package com.imzqqq.app.ui.robot

import android.view.View
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaDialogInteractions.clickDialogNegativeButton
import com.adevinta.android.barista.interaction.BaristaDialogInteractions.clickDialogPositiveButton
import com.adevinta.android.barista.interaction.BaristaDrawerInteractions.openDrawer
import com.imzqqq.app.EspressoHelper
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.waitUntilActivityVisible
import com.imzqqq.app.espresso.tools.waitUntilViewVisible
import com.imzqqq.app.features.createdirect.CreateDirectRoomActivity
import com.imzqqq.app.features.home.HomeActivity
import com.imzqqq.app.features.login.LoginActivity
import com.imzqqq.app.initialSyncIdlingResource
import com.imzqqq.app.ui.robot.settings.SettingsRobot
import com.imzqqq.app.withIdlingResource
import timber.log.Timber

class ElementRobot {

    fun signUp(userId: String) {
        val onboardingRobot = OnboardingRobot()
        onboardingRobot.createAccount(userId = userId)
        waitForHome()
    }

    fun login(userId: String) {
        val onboardingRobot = OnboardingRobot()
        onboardingRobot.login(userId = userId)
        waitForHome()
    }

    private fun waitForHome() {
        waitUntilActivityVisible<HomeActivity> {
            waitUntilViewVisible(withId(R.id.roomListContainer))
        }
        val activity = EspressoHelper.getCurrentActivity()!!
        val uiSession = (activity as HomeActivity).activeSessionHolder.getActiveSession()
        withIdlingResource(initialSyncIdlingResource(uiSession)) {
            waitUntilViewVisible(withId(R.id.bottomNavigationView))
        }
    }

    fun settings(block: SettingsRobot.() -> Unit) {
        openDrawer()
        clickOn(R.id.homeDrawerHeaderSettingsView)
        block(SettingsRobot())
        pressBack()
        waitUntilViewVisible(withId(R.id.bottomNavigationView))
    }

    fun newDirectMessage(block: NewDirectMessageRobot.() -> Unit) {
        clickOn(R.id.bottom_action_people)
        clickOn(R.id.createChatRoomButton)
        waitUntilActivityVisible<CreateDirectRoomActivity> {
            waitUntilViewVisible(withId(R.id.userListSearch))
        }
        // close keyboard
        pressBack()
        block(NewDirectMessageRobot())
        pressBack()
        waitUntilViewVisible(withId(R.id.bottomNavigationView))
    }

    fun newRoom(block: NewRoomRobot.() -> Unit) {
        clickOn(R.id.bottom_action_rooms)
        RoomListRobot().newRoom { block() }
        waitUntilViewVisible(withId(R.id.bottomNavigationView))
    }

    fun roomList(block: RoomListRobot.() -> Unit) {
        clickOn(R.id.bottom_action_rooms)
        block(RoomListRobot())
        waitUntilViewVisible(withId(R.id.bottomNavigationView))
    }

    fun signout(expectSignOutWarning: Boolean) {
        clickOn(R.id.groupToolbarAvatarImageView)
        clickOn(R.id.homeDrawerHeaderSignoutView)

        val isShowingSignOutWarning = kotlin.runCatching {
            waitUntilViewVisible(withId(R.id.exitAnywayButton))
        }.isSuccess

        if (expectSignOutWarning != isShowingSignOutWarning) {
            Timber.w("Unexpected sign out flow, expected warning to be: ${expectSignOutWarning.toWarningType()} but was ${isShowingSignOutWarning.toWarningType()}")
        }

        if (isShowingSignOutWarning) {
            // We have sent a message in a e2e room, accept to loose it
            clickOn(R.id.exitAnywayButton)
            // Dark pattern
            waitUntilViewVisible(withId(android.R.id.button2))
            clickDialogNegativeButton()
        } else {
            waitUntilViewVisible(withId(android.R.id.button1))
            clickDialogPositiveButton()
        }

        waitUntilActivityVisible<LoginActivity> {
            assertDisplayed(R.id.loginSplashLogo)
        }
    }

    fun dismissVerificationIfPresent() {
        kotlin.runCatching {
            Thread.sleep(6000)
            val activity = EspressoHelper.getCurrentActivity()!!
            val popup = activity.findViewById<View>(com.tapadoo.alerter.R.id.llAlertBackground)!!
            activity.runOnUiThread { popup.performClick() }

            waitUntilViewVisible(withId(R.id.bottomSheetFragmentContainer))
            waitUntilViewVisible(ViewMatchers.withText(R.string.skip))
            clickOn(R.string.skip)
            assertDisplayed(R.string.are_you_sure)
            clickOn(R.string.skip)
            waitUntilViewVisible(withId(R.id.bottomSheetFragmentContainer))
        }.onFailure { Timber.w("Verification popup missing", it) }
    }
}

private fun Boolean.toWarningType() = if (this) "shown" else "skipped"

fun ElementRobot.withDeveloperMode(block: ElementRobot.() -> Unit) {
    settings { toggleDeveloperMode() }
    block()
    settings { toggleDeveloperMode() }
}
