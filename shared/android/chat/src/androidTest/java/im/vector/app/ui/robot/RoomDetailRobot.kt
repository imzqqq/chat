package com.imzqqq.app.ui.robot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.interaction.BaristaClickInteractions
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaClickInteractions.longClickOn
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.openMenu
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.waitUntilViewVisible
import com.imzqqq.app.features.home.room.detail.timeline.action.MessageActionsBottomSheet
import com.imzqqq.app.features.reactions.data.EmojiDataSource
import com.imzqqq.app.interactWithSheet
import com.imzqqq.app.waitForView
import java.lang.Thread.sleep

class RoomDetailRobot {

    fun postMessage(content: String) {
        writeTo(R.id.composerEditText, content)
        waitUntilViewVisible(withId(R.id.sendButton))
        clickOn(R.id.sendButton)
        waitUntilViewVisible(withText(content))
    }

    fun crawl() {
        clickOn(R.id.attachmentButton)
        BaristaClickInteractions.clickBack()

        // Menu
        openMenu()
        pressBack()
        clickMenu(R.id.voice_call)
        pressBack()
        clickMenu(R.id.video_call)
        pressBack()
        clickMenu(R.id.search)
        pressBack()
    }

    fun crawlMessage(message: String) {
        // Test quick reaction
        val quickReaction = EmojiDataSource.quickEmojis[0] // 👍
        openMessageMenu(message) {
            addQuickReaction(quickReaction)
        }
        // Open reactions
        longClickOn(quickReaction)
        // wait for bottom sheet
        pressBack()
        // Test add reaction
        openMessageMenu(message) {
            addReactionFromEmojiPicker()
        }
        // Test Edit mode
        openMessageMenu(message) {
            edit()
        }
        // TODO Cancel action
        writeTo(R.id.composerEditText, "Hello universe!")
        // Wait a bit for the keyboard layout to update
        waitUntilViewVisible(withId(R.id.sendButton))
        clickOn(R.id.sendButton)
        // Wait for the UI to update
        waitUntilViewVisible(withText("Hello universe! (edited)"))
        // Open edit history
        openMessageMenu("Hello universe! (edited)") {
            editHistory()
        }
    }

    fun openMessageMenu(message: String, block: MessageMenuRobot.() -> Unit) {
        onView(withId(R.id.timelineRecyclerView))
                .perform(
                        RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                                ViewMatchers.hasDescendant(ViewMatchers.withText(message)),
                                ViewActions.longClick()
                        )
                )
        interactWithSheet<MessageActionsBottomSheet>(contentMatcher = withId(R.id.bottomSheetRecyclerView)) {
            val messageMenuRobot = MessageMenuRobot()
            block(messageMenuRobot)
            if (!messageMenuRobot.autoClosed) {
                pressBack()
            }
        }
    }

    fun openSettings(block: RoomSettingsRobot.() -> Unit) {
        clickOn(R.id.roomToolbarTitleView)
        waitForView(withId(R.id.roomProfileAvatarView))
        sleep(1000)
        block(RoomSettingsRobot())
        pressBack()
    }
}
