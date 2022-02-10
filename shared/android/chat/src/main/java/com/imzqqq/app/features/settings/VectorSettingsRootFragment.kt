package com.imzqqq.app.features.settings

import com.imzqqq.app.R
import com.imzqqq.app.core.preference.VectorPreference
import javax.inject.Inject

class VectorSettingsRootFragment @Inject constructor() : VectorSettingsBaseFragment() {

    override var titleRes: Int = R.string.title_activity_settings
    override val preferenceXmlRes = R.xml.vector_settings_root

    override fun bindPref() {
        tintIcons()
    }

    private fun tintIcons() {
        for (i in 0 until preferenceScreen.preferenceCount) {
            (preferenceScreen.getPreference(i) as? VectorPreference)?.let { it.tintIcon = true }
        }
    }
}
