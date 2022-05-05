package com.imzqqq.app.flow.components.preference

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.imzqqq.app.R
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.entity.Notification
import com.imzqqq.app.flow.settings.AppTheme
import com.imzqqq.app.flow.settings.PrefKeys
import com.imzqqq.app.flow.settings.emojiPreference
import com.imzqqq.app.flow.settings.listPreference
import com.imzqqq.app.flow.settings.makePreferenceScreen
import com.imzqqq.app.flow.settings.preference
import com.imzqqq.app.flow.settings.preferenceCategory
import com.imzqqq.app.flow.settings.switchPreference
import com.imzqqq.app.flow.util.ThemeUtils
import com.imzqqq.app.flow.util.deserialize
import com.imzqqq.app.flow.util.getNonNullString
import com.imzqqq.app.flow.util.serialize
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizePx
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import javax.inject.Inject

@AndroidEntryPoint
class PreferencesFragment : PreferenceFragmentCompat() {

    @Inject lateinit var okhttpclient: OkHttpClient
    @Inject lateinit var accountManager: AccountManager

    private val iconSize by lazy { resources.getDimensionPixelSize(R.dimen.preference_icon_size) }
    private var httpProxyPref: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        makePreferenceScreen {
            preferenceCategory(R.string.pref_title_appearance_settings) {
                listPreference {
                    setDefaultValue(AppTheme.NIGHT.value)
                    setEntries(R.array.app_theme_names)
                    entryValues = AppTheme.stringValues()
                    key = PrefKeys.APP_THEME
                    setSummaryProvider { entry }
                    setTitle(R.string.pref_title_app_theme)
                    icon = makeIcon(GoogleMaterial.Icon.gmd_palette)
                }

                emojiPreference(okhttpclient) {
                    setDefaultValue("system_default")
                    setIcon(R.drawable.ic_emoji_24dp)
                    key = PrefKeys.EMOJI
                    setSummary(R.string.system_default)
                    setTitle(R.string.emoji_style)
                    icon = makeIcon(GoogleMaterial.Icon.gmd_sentiment_satisfied)
                }

                listPreference {
                    setDefaultValue("default")
                    setEntries(R.array.language_entries)
                    setEntryValues(R.array.language_values)
                    key = PrefKeys.LANGUAGE
                    setSummaryProvider { entry }
                    setTitle(R.string.pref_title_language)
                    icon = makeIcon(GoogleMaterial.Icon.gmd_translate)
                }

                listPreference {
                    setDefaultValue("medium")
                    setEntries(R.array.status_text_size_names)
                    setEntryValues(R.array.status_text_size_values)
                    key = PrefKeys.STATUS_TEXT_SIZE
                    setSummaryProvider { entry }
                    setTitle(R.string.pref_status_text_size)
                    icon = makeIcon(GoogleMaterial.Icon.gmd_format_size)
                }

                listPreference {
                    setDefaultValue("top")
                    setEntries(R.array.pref_main_nav_position_options)
                    setEntryValues(R.array.pref_main_nav_position_values)
                    key = PrefKeys.MAIN_NAV_POSITION
                    setSummaryProvider { entry }
                    setTitle(R.string.pref_main_nav_position)
                }

                switchPreference {
                    setDefaultValue(false)
                    key = PrefKeys.HIDE_TOP_TOOLBAR
                    setTitle(R.string.pref_title_hide_top_toolbar)
                }

                switchPreference {
                    setDefaultValue(false)
                    key = PrefKeys.FAB_HIDE
                    setTitle(R.string.pref_title_hide_follow_button)
                    isSingleLineTitle = false
                }

                switchPreference {
                    setDefaultValue(false)
                    key = PrefKeys.ABSOLUTE_TIME_VIEW
                    setTitle(R.string.pref_title_absolute_time)
                    isSingleLineTitle = false
                }

                switchPreference {
                    setDefaultValue(true)
                    key = PrefKeys.SHOW_BOT_OVERLAY
                    setTitle(R.string.pref_title_bot_overlay)
                    isSingleLineTitle = false
                    setIcon(R.drawable.ic_bot_24dp)
                }

                switchPreference {
                    setDefaultValue(false)
                    key = PrefKeys.ANIMATE_GIF_AVATARS
                    setTitle(R.string.pref_title_animate_gif_avatars)
                    isSingleLineTitle = false
                }

                switchPreference {
                    setDefaultValue(true)
                    key = PrefKeys.USE_BLURHASH
                    setTitle(R.string.pref_title_gradient_for_media)
                    isSingleLineTitle = false
                }

                switchPreference {
                    setDefaultValue(false)
                    key = PrefKeys.SHOW_CARDS_IN_TIMELINES
                    setTitle(R.string.pref_title_show_cards_in_timelines)
                    isSingleLineTitle = false
                }

                switchPreference {
                    setDefaultValue(true)
                    key = PrefKeys.SHOW_NOTIFICATIONS_FILTER
                    setTitle(R.string.pref_title_show_notifications_filter)
                    isSingleLineTitle = false
                }

                switchPreference {
                    setDefaultValue(true)
                    key = PrefKeys.CONFIRM_REBLOGS
                    setTitle(R.string.pref_title_confirm_reblogs)
                    isSingleLineTitle = false
                }

                switchPreference {
                    setDefaultValue(true)
                    key = PrefKeys.ENABLE_SWIPE_FOR_TABS
                    setTitle(R.string.pref_title_enable_swipe_for_tabs)
                    isSingleLineTitle = false
                }

                switchPreference {
                    setDefaultValue(false)
                    key = PrefKeys.ANIMATE_CUSTOM_EMOJIS
                    setTitle(R.string.pref_title_animate_custom_emojis)
                    isSingleLineTitle = false
                }
            }

            preferenceCategory(R.string.pref_title_browser_settings) {
                switchPreference {
                    setDefaultValue(false)
                    key = PrefKeys.CUSTOM_TABS
                    setTitle(R.string.pref_title_custom_tabs)
                    isSingleLineTitle = false
                }
            }

            preferenceCategory(R.string.pref_title_timeline_filters) {
                preference {
                    setTitle(R.string.pref_title_status_tabs)
                    setOnPreferenceClickListener {
                        activity?.let { activity ->
                            val intent = PreferencesActivity.newIntent(activity, PreferencesActivity.TAB_FILTER_PREFERENCES)
                            activity.startActivity(intent)
                            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                        }
                        true
                    }
                }
            }

            preferenceCategory(R.string.pref_title_wellbeing_mode) {
                switchPreference {
                    title = getString(R.string.limit_notifications)
                    setDefaultValue(false)
                    key = PrefKeys.WELLBEING_LIMITED_NOTIFICATIONS
                    setOnPreferenceChangeListener { _, value ->
                        for (account in accountManager.accounts) {
                            val notificationFilter = deserialize(account.notificationsFilter).toMutableSet()

                            if (value == true) {
                                notificationFilter.add(Notification.Type.FAVOURITE)
                                notificationFilter.add(Notification.Type.FOLLOW)
                                notificationFilter.add(Notification.Type.REBLOG)
                            } else {
                                notificationFilter.remove(Notification.Type.FAVOURITE)
                                notificationFilter.remove(Notification.Type.FOLLOW)
                                notificationFilter.remove(Notification.Type.REBLOG)
                            }

                            account.notificationsFilter = serialize(notificationFilter)
                            accountManager.saveAccount(account)
                        }
                        true
                    }
                }

                switchPreference {
                    title = getString(R.string.wellbeing_hide_stats_posts)
                    setDefaultValue(false)
                    key = PrefKeys.WELLBEING_HIDE_STATS_POSTS
                }

                switchPreference {
                    title = getString(R.string.wellbeing_hide_stats_profile)
                    setDefaultValue(false)
                    key = PrefKeys.WELLBEING_HIDE_STATS_PROFILE
                }
            }

            preferenceCategory(R.string.pref_title_proxy_settings) {
                httpProxyPref = preference {
                    setTitle(R.string.pref_title_http_proxy_settings)
                    setOnPreferenceClickListener {
                        activity?.let { activity ->
                            val intent = PreferencesActivity.newIntent(activity, PreferencesActivity.PROXY_PREFERENCES)
                            activity.startActivity(intent)
                            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                        }
                        true
                    }
                }
            }
        }
    }

    private fun makeIcon(icon: GoogleMaterial.Icon): IconicsDrawable {
        val context = requireContext()
        return IconicsDrawable(context, icon).apply {
            sizePx = iconSize
            colorInt = ThemeUtils.getColor(context, R.attr.iconColor)
        }
    }

    override fun onResume() {
        super.onResume()
        updateHttpProxySummary()
    }

    private fun updateHttpProxySummary() {
        val sharedPreferences = preferenceManager.sharedPreferences
        val httpProxyEnabled = sharedPreferences.getBoolean(PrefKeys.HTTP_PROXY_ENABLED, false)
        val httpServer = sharedPreferences.getNonNullString(PrefKeys.HTTP_PROXY_SERVER, "")

        try {
            val httpPort = sharedPreferences.getNonNullString(PrefKeys.HTTP_PROXY_PORT, "-1")
                .toInt()

            if (httpProxyEnabled && httpServer.isNotBlank() && httpPort > 0 && httpPort < 65535) {
                httpProxyPref?.summary = "$httpServer:$httpPort"
                return
            }
        } catch (e: NumberFormatException) {
            // user has entered wrong port, fall back to empty summary
        }

        httpProxyPref?.summary = ""
    }

    companion object {
        fun newInstance(): PreferencesFragment {
            return PreferencesFragment()
        }
    }
}
