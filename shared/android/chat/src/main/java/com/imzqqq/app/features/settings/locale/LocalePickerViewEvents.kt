package com.imzqqq.app.features.settings.locale

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class LocalePickerViewEvents : VectorViewEvents {
    object RestartActivity : LocalePickerViewEvents()
}
