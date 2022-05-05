package com.imzqqq.app.flow.components.preference

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ActivityPreferencesBinding
import com.imzqqq.app.flow.BaseActivity
import com.imzqqq.app.flow.FlowActivity
import com.imzqqq.app.flow.appstore.EventHub
import com.imzqqq.app.flow.appstore.PreferenceChangedEvent
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.util.ThemeUtils
import com.imzqqq.app.flow.util.getNonNullString
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PreferencesActivity : BaseActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject lateinit var eventHub: EventHub

    private var restartActivitiesOnExit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val fragmentTag = "preference_fragment_$EXTRA_PREFERENCE_TYPE"

        val fragment: Fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
            ?: when (intent.getIntExtra(EXTRA_PREFERENCE_TYPE, 0)) {
                GENERAL_PREFERENCES -> {
                    setTitle(R.string.action_view_preferences)
                    PreferencesFragment.newInstance()
                }
                ACCOUNT_PREFERENCES -> {
                    setTitle(R.string.action_view_account_preferences)
                    AccountPreferencesFragment.newInstance()
                }
                NOTIFICATION_PREFERENCES -> {
                    setTitle(R.string.pref_title_edit_notification_settings)
                    NotificationPreferencesFragment.newInstance()
                }
                TAB_FILTER_PREFERENCES -> {
                    setTitle(R.string.pref_title_status_tabs)
                    TabFilterPreferencesFragment.newInstance()
                }
                PROXY_PREFERENCES -> {
                    setTitle(R.string.pref_title_http_proxy_settings)
                    ProxyPreferencesFragment.newInstance()
                }
                else -> throw IllegalArgumentException("preferenceType not known")
            }

        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment, fragmentTag)
        }

        restartActivitiesOnExit = intent.getBooleanExtra("restart", false)
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun saveInstanceState(outState: Bundle) {
        outState.putBoolean("restart", restartActivitiesOnExit)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("restart", restartActivitiesOnExit)
        super.onSaveInstanceState(outState)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "appTheme"                                                                                                                  -> {
                val theme = sharedPreferences.getNonNullString("appTheme", ThemeUtils.APP_THEME_DEFAULT)
                Timber.d(theme)
                ThemeUtils.setAppNightMode(theme)

                restartActivitiesOnExit = true
                this.restartCurrentActivity()
            }
            "statusTextSize", "absoluteTimeView", "showBotOverlay", "animateGifAvatars",
            "useBlurhash", "showCardsInTimelines", "confirmReblogs", "enableSwipeForTabs", "mainNavPosition", PrefKeys.HIDE_TOP_TOOLBAR -> {
                restartActivitiesOnExit = true
            }
            "language"                                                                                                                  -> {
                restartActivitiesOnExit = true
                this.restartCurrentActivity()
            }
        }

        eventHub.dispatch(PreferenceChangedEvent(key))
    }

    private fun restartCurrentActivity() {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val savedInstanceState = Bundle()
        saveInstanceState(savedInstanceState)
        intent.putExtras(savedInstanceState)
        startActivityWithSlideInAnimation(intent)
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onBackPressed() {
        /* Switching themes won't actually change the theme of activities on the back stack.
         * Either the back stack activities need to all be recreated, or do the easier thing, which
         * is hijack the back button press and use it to launch a new MainActivity and clear the
         * back stack. */
        if (restartActivitiesOnExit) {
            val intent = Intent(this, FlowActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivityWithSlideInAnimation(intent)
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        const val GENERAL_PREFERENCES = 0
        const val ACCOUNT_PREFERENCES = 1
        const val NOTIFICATION_PREFERENCES = 2
        const val TAB_FILTER_PREFERENCES = 3
        const val PROXY_PREFERENCES = 4
        private const val EXTRA_PREFERENCE_TYPE = "EXTRA_PREFERENCE_TYPE"

        @JvmStatic
        fun newIntent(context: Context, preferenceType: Int): Intent {
            val intent = Intent(context, PreferencesActivity::class.java)
            intent.putExtra(EXTRA_PREFERENCE_TYPE, preferenceType)
            return intent
        }
    }
}
