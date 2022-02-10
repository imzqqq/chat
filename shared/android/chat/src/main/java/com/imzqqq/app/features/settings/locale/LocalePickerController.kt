package com.imzqqq.app.features.settings.locale

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Incomplete
import com.airbnb.mvrx.Success
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.epoxy.noResultItem
import com.imzqqq.app.core.epoxy.profiles.profileSectionItem
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.utils.safeCapitalize
import com.imzqqq.app.features.settings.VectorLocale
import com.imzqqq.app.features.settings.VectorPreferences
import java.util.Locale
import javax.inject.Inject

class LocalePickerController @Inject constructor(
        private val vectorPreferences: VectorPreferences,
        private val stringProvider: StringProvider
) : TypedEpoxyController<LocalePickerViewState>() {

    var listener: Listener? = null

    override fun buildModels(data: LocalePickerViewState?) {
        val list = data?.locales ?: return
        val host = this

        profileSectionItem {
            id("currentTitle")
            title(host.stringProvider.getString(R.string.choose_locale_current_locale_title))
        }
        localeItem {
            id(data.currentLocale.toString())
            title(VectorLocale.localeToLocalisedString(data.currentLocale).safeCapitalize(data.currentLocale))
            if (host.vectorPreferences.developerMode()) {
                subtitle(VectorLocale.localeToLocalisedStringInfo(data.currentLocale))
            }
            clickListener { host.listener?.onUseCurrentClicked() }
        }
        profileSectionItem {
            id("otherTitle")
            title(host.stringProvider.getString(R.string.choose_locale_other_locales_title))
        }
        when (list) {
            is Incomplete -> {
                loadingItem {
                    id("loading")
                    loadingText(host.stringProvider.getString(R.string.choose_locale_loading_locales))
                }
            }
            is Success    ->
                if (list().isEmpty()) {
                    noResultItem {
                        id("noResult")
                        text(host.stringProvider.getString(R.string.no_result_placeholder))
                    }
                } else {
                    list()
                            .filter { it.toString() != data.currentLocale.toString() }
                            .forEach { locale ->
                                localeItem {
                                    id(locale.toString())
                                    title(VectorLocale.localeToLocalisedString(locale).safeCapitalize(locale))
                                    if (host.vectorPreferences.developerMode()) {
                                        subtitle(VectorLocale.localeToLocalisedStringInfo(locale))
                                    }
                                    clickListener { host.listener?.onLocaleClicked(locale) }
                                }
                            }
                }
        }
    }

    interface Listener {
        fun onUseCurrentClicked()
        fun onLocaleClicked(locale: Locale)
    }
}
