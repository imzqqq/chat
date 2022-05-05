/* Copyright 2018 Conny Duck

 * This file is a part of Flow.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Flow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Flow; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.flow.components.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.keylesspalace.flow.R
import com.keylesspalace.flow.settings.PrefKeys
import com.keylesspalace.flow.settings.editTextPreference
import com.keylesspalace.flow.settings.makePreferenceScreen
import com.keylesspalace.flow.settings.switchPreference
import kotlin.system.exitProcess

class ProxyPreferencesFragment : PreferenceFragmentCompat() {
    private var pendingRestart = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        makePreferenceScreen {
            switchPreference {
                setTitle(R.string.pref_title_http_proxy_enable)
                isIconSpaceReserved = false
                key = PrefKeys.HTTP_PROXY_ENABLED
                setDefaultValue(false)
            }

            editTextPreference {
                setTitle(R.string.pref_title_http_proxy_server)
                key = PrefKeys.HTTP_PROXY_SERVER
                isIconSpaceReserved = false
                setSummaryProvider { text }
            }

            editTextPreference {
                setTitle(R.string.pref_title_http_proxy_port)
                key = PrefKeys.HTTP_PROXY_PORT
                isIconSpaceReserved = false
                setSummaryProvider { text }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (pendingRestart) {
            pendingRestart = false
            exitProcess(0)
        }
    }

    companion object {
        fun newInstance(): ProxyPreferencesFragment {
            return ProxyPreferencesFragment()
        }
    }
}
