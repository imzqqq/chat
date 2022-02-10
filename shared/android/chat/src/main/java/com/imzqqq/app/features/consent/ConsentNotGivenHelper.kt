package com.imzqqq.app.features.consent

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.dialogs.DialogLocker
import com.imzqqq.app.core.platform.Restorable
import com.imzqqq.app.features.webview.VectorWebViewActivity
import com.imzqqq.app.features.webview.WebViewMode

class ConsentNotGivenHelper(private val activity: Activity,
                            private val dialogLocker: DialogLocker) :
        Restorable by dialogLocker {

    /* ==========================================================================================
     * Public methods
     * ========================================================================================== */

    /**
     * Display the consent dialog, if not already displayed
     */
    fun displayDialog(consentUri: String, homeServerHost: String) {
        dialogLocker.displayDialog {
            MaterialAlertDialogBuilder(activity)
                    .setTitle(R.string.settings_app_term_conditions)
                    .setMessage(activity.getString(R.string.dialog_user_consent_content, homeServerHost))
                    .setPositiveButton(R.string.dialog_user_consent_submit) { _, _ ->
                        openWebViewActivity(consentUri)
                    }
        }
    }

    /* ==========================================================================================
     * Private
     * ========================================================================================== */

    private fun openWebViewActivity(consentUri: String) {
        val intent = VectorWebViewActivity.getIntent(activity, consentUri, activity.getString(R.string.settings_app_term_conditions), WebViewMode.CONSENT)
        activity.startActivity(intent)
    }
}
