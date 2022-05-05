package com.imzqqq.app.core.dialogs

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.core.platform.Restorable
import timber.log.Timber

private const val KEY_DIALOG_IS_DISPLAYED = "DialogLocker.KEY_DIALOG_IS_DISPLAYED"

/**
 * Class to avoid displaying twice the same dialog
 */
class DialogLocker(savedInstanceState: Bundle?) : Restorable {

    private var isDialogDisplayed = savedInstanceState?.getBoolean(KEY_DIALOG_IS_DISPLAYED, false) == true

    private fun unlock() {
        isDialogDisplayed = false
    }

    private fun lock() {
        isDialogDisplayed = true
    }

    fun displayDialog(builder: () -> MaterialAlertDialogBuilder): AlertDialog? {
        return if (isDialogDisplayed) {
            Timber.w("Filtered dialog request")
            null
        } else {
            builder
                    .invoke()
                    .create()
                    .apply {
                        setOnShowListener { lock() }
                        setOnCancelListener { unlock() }
                        setOnDismissListener { unlock() }
                        show()
                    }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_DIALOG_IS_DISPLAYED, isDialogDisplayed)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        isDialogDisplayed = savedInstanceState?.getBoolean(KEY_DIALOG_IS_DISPLAYED, false) == true
    }
}
