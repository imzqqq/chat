package com.imzqqq.app.ui.robot

import com.adevinta.android.barista.interaction.BaristaDialogInteractions.clickDialogNegativeButton

class DialogRobot(
        var returnedToPreviousScreen: Boolean = false
) {

    fun negativeAction() {
        clickDialogNegativeButton()
        returnedToPreviousScreen = true
    }
}
