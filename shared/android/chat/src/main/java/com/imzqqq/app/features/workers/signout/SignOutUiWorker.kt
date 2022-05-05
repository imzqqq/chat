package com.imzqqq.app.features.workers.signout

import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.cannotLogoutSafely
import com.imzqqq.app.core.extensions.singletonEntryPoint
import com.imzqqq.app.features.MainActivity
import com.imzqqq.app.features.MainActivityArgs

class SignOutUiWorker(private val activity: FragmentActivity) {

    fun perform() {
        val session = activity.singletonEntryPoint().activeSessionHolder().getSafeActiveSession() ?: return
        if (session.cannotLogoutSafely()) {
            // The backup check on logout flow has to be displayed if there are keys in the store, and the keys backup state is not Ready
            val signOutDialog = SignOutBottomSheetDialogFragment.newInstance()
            signOutDialog.onSignOut = Runnable {
                doSignOut()
            }
            signOutDialog.show(activity.supportFragmentManager, "SO")
        } else {
            // Display a simple confirmation dialog
            MaterialAlertDialogBuilder(activity)
                    .setTitle(R.string.action_sign_out)
                    .setMessage(R.string.action_sign_out_confirmation_simple)
                    .setPositiveButton(R.string.action_sign_out) { _, _ ->
                        doSignOut()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
        }
    }

    private fun doSignOut() {
        MainActivity.restartApp(activity, MainActivityArgs(clearCredentials = true))
    }
}
