package com.imzqqq.app.features.settings

import androidx.preference.Preference
import androidx.preference.SeekBarPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.core.preference.VectorPreference
import com.imzqqq.app.core.preference.VectorPreferenceCategory
import com.imzqqq.app.core.preference.VectorSwitchPreference
import com.imzqqq.app.features.rageshake.RageShake
import javax.inject.Inject

class VectorSettingsAdvancedSettingsFragment @Inject constructor(
        private val vectorPreferences: VectorPreferences
): VectorSettingsBaseFragment() {

    override var titleRes = R.string.settings_advanced_settings
    override val preferenceXmlRes = R.xml.vector_settings_advanced_settings

    private var rageshake: RageShake? = null

    override fun onResume() {
        super.onResume()

        rageshake = (activity as? VectorBaseActivity<*>)?.rageShake
        rageshake?.interceptor = {
            (activity as? VectorBaseActivity<*>)?.showSnackbar(getString(R.string.rageshake_detected))
        }
    }

    override fun onPause() {
        super.onPause()
        rageshake?.interceptor = null
        rageshake = null
    }

    override fun bindPref() {
        val isRageShakeAvailable = RageShake.isAvailable(requireContext())

        if (isRageShakeAvailable) {
            findPreference<VectorSwitchPreference>(VectorPreferences.SETTINGS_USE_RAGE_SHAKE_KEY)!!
                    .onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->

                if (newValue as? Boolean == true) {
                    rageshake?.start()
                } else {
                    rageshake?.stop()
                }

                true
            }

            findPreference<SeekBarPreference>(VectorPreferences.SETTINGS_RAGE_SHAKE_DETECTION_THRESHOLD_KEY)!!
                    .onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                (activity as? VectorBaseActivity<*>)?.let {
                    val newValueAsInt = newValue as? Int ?: return@OnPreferenceChangeListener true

                    rageshake?.setSensitivity(newValueAsInt)
                }

                true
            }
        } else {
            findPreference<VectorPreferenceCategory>("SETTINGS_RAGE_SHAKE_CATEGORY_KEY")!!.isVisible = false
        }

        findPreference<VectorPreference>("SETTINGS_APPLY_SC_DEFAULT_SETTINGS")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.settings_apply_sc_default_settings_dialog_title)
                    .setMessage(R.string.settings_apply_sc_default_settings_dialog_summary)
                    .setPositiveButton(R.string._continue) { _, _ ->
                        vectorPreferences.applyScDefaultValues()
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> /* Just close dialog */ }
                    .show()
            true
        }
    }
}
