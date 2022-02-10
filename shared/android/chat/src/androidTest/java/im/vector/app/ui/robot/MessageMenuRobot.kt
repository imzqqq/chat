package com.imzqqq.app.ui.robot

import androidx.test.espresso.Espresso.pressBack
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.imzqqq.app.R
import java.lang.Thread.sleep

class MessageMenuRobot(
        var autoClosed: Boolean = false
) {

    fun viewSource() {
        clickOn(R.string.view_source)
        // wait for library
        sleep(1000)
        pressBack()
        autoClosed = true
    }

    fun editHistory() {
        clickOn(R.string.message_view_edit_history)
        pressBack()
        autoClosed = true
    }

    fun addQuickReaction(quickReaction: String) {
        clickOn(quickReaction)
        autoClosed = true
    }

    fun addReactionFromEmojiPicker() {
        clickOn(R.string.message_add_reaction)
        // Wait for emoji to load, it's async now
        sleep(2000)
        clickListItem(R.id.emojiRecyclerView, 4)
        autoClosed = true
    }

    fun edit() {
        clickOn(R.string.edit)
        autoClosed = true
    }
}
