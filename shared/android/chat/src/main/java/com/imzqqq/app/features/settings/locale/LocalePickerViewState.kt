package com.imzqqq.app.features.settings.locale

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.imzqqq.app.features.settings.VectorLocale
import java.util.Locale

data class LocalePickerViewState(
        val currentLocale: Locale = VectorLocale.applicationLocale,
        val locales: Async<List<Locale>> = Uninitialized
) : MavericksState
