package com.imzqqq.app.flow.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import java.util.Locale

class LocaleManager(context: Context) {

    private var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun setLocale(context: Context): Context {
        val language = prefs.getNonNullString("language", "default")
        if (language == "default") {
            return context
        }
        val locale = Locale.forLanguageTag(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
