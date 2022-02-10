package com.imzqqq.app.features.configuration

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import com.imzqqq.app.features.settings.FontScale
import com.imzqqq.app.features.settings.VectorLocale
import com.imzqqq.app.features.themes.ThemeUtils
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

/**
 * Handle locale configuration change, such as theme, font size and locale chosen by the user
 */
class VectorConfiguration @Inject constructor(private val context: Context) {

    fun onConfigurationChanged() {
        if (Locale.getDefault().toString() != VectorLocale.applicationLocale.toString()) {
            Timber.v("## onConfigurationChanged(): the locale has been updated to ${Locale.getDefault()}")
            Timber.v("## onConfigurationChanged(): restore the expected value ${VectorLocale.applicationLocale}")
            Locale.setDefault(VectorLocale.applicationLocale)
        }
        if (ThemeUtils.useDarkTheme(context) != ThemeUtils.shouldUseDarkTheme(context)) {
            Timber.v("## onConfigurationChanged(): night mode has changed")
            ThemeUtils.invalidateNightMode(context)
        }
        // Night mode may have changed
        ThemeUtils.init(context)
    }

    fun applyToApplicationContext() {
        val locale = VectorLocale.applicationLocale
        val fontScale = FontScale.getFontScaleValue(context)

        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        @Suppress("DEPRECATION")
        config.locale = locale
        config.fontScale = fontScale.scale
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    /**
     * Compute a localised context
     *
     * @param context the context
     * @return the localised context
     */
    fun getLocalisedContext(context: Context): Context {
        try {
            val locale = VectorLocale.applicationLocale

            // create new configuration passing old configuration from original Context
            val configuration = Configuration(context.resources.configuration)

            configuration.fontScale = FontScale.getFontScaleValue(context).scale

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setLocaleForApi24(configuration, locale)
            } else {
                configuration.setLocale(locale)
            }
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        } catch (e: Exception) {
            Timber.e(e, "## getLocalisedContext() failed")
        }

        return context
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setLocaleForApi24(config: Configuration, locale: Locale) {
        val set: MutableSet<Locale> = LinkedHashSet()
        // bring the user locale to the front of the list
        set.add(locale)
        val all = LocaleList.getDefault()
        for (i in 0 until all.size()) {
            // append other locales supported by the user
            set.add(all[i])
        }
        val locales = set.toTypedArray()
        config.setLocales(LocaleList(*locales))
    }

    /**
     * Compute the locale status value
     * @return the local status value
     */
    fun getHash(): String {
        return (VectorLocale.applicationLocale.toString() +
                "_" + FontScale.getFontScaleValue(context).preferenceValue +
                "_" + ThemeUtils.getApplicationLightTheme(context) +
                "_" + ThemeUtils.getApplicationDarkTheme(context) +
                "_" + ThemeUtils.getApplicationLightThemeAccent(context) +
                "_" + ThemeUtils.getApplicationDarkThemeAccent(context) +
                "_" + ThemeUtils.useDarkTheme(context).toString())
    }
}
