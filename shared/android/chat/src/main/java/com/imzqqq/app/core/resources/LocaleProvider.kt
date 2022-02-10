package com.imzqqq.app.core.resources

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import java.util.Locale
import javax.inject.Inject

class LocaleProvider @Inject constructor(private val resources: Resources) {

    fun current(): Locale {
        return ConfigurationCompat.getLocales(resources.configuration)[0]
    }
}
