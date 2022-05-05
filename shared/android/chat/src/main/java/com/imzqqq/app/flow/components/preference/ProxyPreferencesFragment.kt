package com.imzqqq.app.flow.components.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.imzqqq.app.R
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.settings.editTextPreference
import com.imzqqq.app.flow.settings.makePreferenceScreen
import com.imzqqq.app.flow.settings.switchPreference
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
