package com.imzqqq.app.features.settings

import android.os.Build
import com.imzqqq.app.R
import com.imzqqq.app.core.preference.VectorSwitchPreference
import com.imzqqq.app.features.themes.ThemeUtils
import javax.inject.Inject

class VectorSettingsLabsFragment @Inject constructor(
        private val vectorPreferences: VectorPreferences
) : VectorSettingsBaseFragment() {

    override var titleRes = R.string.room_settings_labs_pref_title
    override val preferenceXmlRes = R.xml.vector_settings_labs

    override fun bindPref() {
        // Lab

        val systemDarkThemePreTenPref = findPreference<VectorSwitchPreference>(ThemeUtils.SYSTEM_DARK_THEME_PRE_TEN)
        systemDarkThemePreTenPref?.let {
            if (ThemeUtils.darkThemeDefinitivelyPossible()) {
                it.parent?.removePreference(it)
            }
        }
        /*
        systemDarkThemePreTenPref?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue is Boolean) {
                if (ThemeUtils.shouldUseDarkTheme(requireContext())) {
                    // Restart the Activity | TODO: we need to do this AFTER the value is persisted...
                    activity?.restart()
                }
                true
            } else {
                false
            }
        }
         */

        findPreference<VectorSwitchPreference>(VectorPreferences.SETTINGS_ALLOW_URL_PREVIEW_IN_ENCRYPTED_ROOM_KEY)?.isEnabled = vectorPreferences.showUrlPreviews()

        findPreference<VectorSwitchPreference>(VectorPreferences.SETTINGS_VOICE_MESSAGE)?.isEnabled = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP

    }
}
