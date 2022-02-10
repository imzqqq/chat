package com.imzqqq.app.features.settings

import androidx.preference.Preference
import com.imzqqq.app.core.preference.ColorMatrixListPreference
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.restart
import com.imzqqq.app.features.themes.ThemeUtils
import javax.inject.Inject

class VectorSettingsAdvancedThemeFragment @Inject constructor(
        //private val vectorPreferences: VectorPreferences
) : VectorSettingsBaseFragment() {

    override var titleRes = R.string.settings_advanced_theme_settings
    override val preferenceXmlRes = R.xml.vector_settings_advanced_theme_settings

    override fun bindPref() {
        val lightAccentPref = findPreference<ColorMatrixListPreference>(ThemeUtils.SETTINGS_SC_ACCENT_LIGHT)!!
        val darkAccentPref = findPreference<ColorMatrixListPreference>(ThemeUtils.SETTINGS_SC_ACCENT_DARK)!!

        lightAccentPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue is String) {
                ThemeUtils.setApplicationLightThemeAccent(requireContext().applicationContext, newValue)
                if (ThemeUtils.isLightTheme(requireContext())) {
                    // Restart the Activity
                    activity?.restart()
                }
                true
            } else {
                false
            }
        }

        darkAccentPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue is String) {
                ThemeUtils.setApplicationDarkThemeAccent(requireContext().applicationContext, newValue)
                if (!ThemeUtils.isLightTheme(requireContext())) {
                    // Restart the Activity
                    activity?.restart()
                }
                true
            } else {
                false
            }
        }
    }
}
