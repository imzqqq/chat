package com.imzqqq.app.flow.components.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.imzqqq.app.R
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.settings.checkBoxPreference
import com.imzqqq.app.flow.settings.makePreferenceScreen
import com.imzqqq.app.flow.settings.preferenceCategory

class TabFilterPreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        makePreferenceScreen {
            preferenceCategory(R.string.title_home) { category ->
                category.isIconSpaceReserved = false

                checkBoxPreference {
                    setTitle(R.string.pref_title_show_boosts)
                    key = PrefKeys.TAB_FILTER_HOME_BOOSTS
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }

                checkBoxPreference {
                    setTitle(R.string.pref_title_show_replies)
                    key = PrefKeys.TAB_FILTER_HOME_REPLIES
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            }
        }
    }

    companion object {
        fun newInstance(): TabFilterPreferencesFragment {
            return TabFilterPreferencesFragment()
        }
    }
}
