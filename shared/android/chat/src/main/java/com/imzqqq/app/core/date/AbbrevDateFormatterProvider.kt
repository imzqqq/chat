package com.imzqqq.app.core.date

import android.text.format.DateFormat
import com.imzqqq.app.core.resources.LocaleProvider
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class AbbrevDateFormatterProvider @Inject constructor(private val localeProvider: LocaleProvider) : DateFormatterProvider {

    override val dateWithMonthFormatter: DateTimeFormatter by lazy {
        val pattern = DateFormat.getBestDateTimePattern(localeProvider.current(), "d MMM")
        DateTimeFormatter.ofPattern(pattern, localeProvider.current())
    }

    override val dateWithYearFormatter: DateTimeFormatter by lazy {
        val pattern = DateFormat.getBestDateTimePattern(localeProvider.current(), "dd.MM.yyyy")
        DateTimeFormatter.ofPattern(pattern, localeProvider.current())
    }
}
