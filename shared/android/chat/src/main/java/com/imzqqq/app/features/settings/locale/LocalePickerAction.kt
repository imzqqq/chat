package com.imzqqq.app.features.settings.locale

import com.imzqqq.app.core.platform.VectorViewModelAction
import java.util.Locale

sealed class LocalePickerAction : VectorViewModelAction {
    data class SelectLocale(val locale: Locale) : LocalePickerAction()
}
