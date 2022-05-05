package com.imzqqq.app.features.rageshake

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.core.content.getSystemService
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.seismic.ShakeDetector
import com.imzqqq.app.R
import com.imzqqq.app.core.hardware.vibrate
import com.imzqqq.app.features.navigation.Navigator
import com.imzqqq.app.features.settings.VectorPreferences
import com.imzqqq.app.features.settings.VectorSettingsActivity
import javax.inject.Inject

class RageShake @Inject constructor(private val activity: FragmentActivity,
                                    private val bugReporter: BugReporter,
                                    private val navigator: Navigator,
                                    private val vectorPreferences: VectorPreferences) : ShakeDetector.Listener {

    private var shakeDetector: ShakeDetector? = null

    private var dialogDisplayed = false

    var interceptor: (() -> Unit)? = null

    fun start() {
        val sensorManager = activity.getSystemService<SensorManager>() ?: return

        shakeDetector = ShakeDetector(this).apply {
            setSensitivity(vectorPreferences.getRageshakeSensitivity())
            start(sensorManager, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        shakeDetector?.stop()
    }

    fun setSensitivity(sensitivity: Int) {
        shakeDetector?.setSensitivity(sensitivity)
    }

    override fun hearShake() {
        val i = interceptor
        if (i != null) {
            vibrate(activity)
            i.invoke()
        } else {
            if (dialogDisplayed) {
                // Filtered!
                return
            }

            vibrate(activity)
            dialogDisplayed = true

            MaterialAlertDialogBuilder(activity)
                    .setMessage(R.string.send_bug_report_alert_message)
                    .setPositiveButton(R.string.yes) { _, _ -> openBugReportScreen() }
                    .setNeutralButton(R.string.settings) { _, _ -> openSettings() }
                    .setOnDismissListener { dialogDisplayed = false }
                    .setNegativeButton(R.string.no, null)
                    .show()
        }
    }

    private fun openBugReportScreen() {
        bugReporter.openBugReportScreen(activity)
    }

    private fun openSettings() {
        navigator.openSettings(activity, VectorSettingsActivity.EXTRA_DIRECT_ACCESS_ADVANCED_SETTINGS)
    }

    companion object {
        /**
         * Check if the feature is available
         */
        fun isAvailable(context: Context): Boolean {
            return context.getSystemService<SensorManager>()?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
        }
    }
}
