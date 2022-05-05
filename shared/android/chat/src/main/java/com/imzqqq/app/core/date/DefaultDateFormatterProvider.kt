package com.imzqqq.app.core.date

import android.content.Context
import android.text.format.DateFormat
import com.imzqqq.app.core.resources.LocaleProvider
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class DefaultDateFormatterProvider @Inject constructor(private val context: Context,
                                                       private val localeProvider: LocaleProvider) :
    DateFormatterProvider {

    override val dateWithMonthFormatter: DateTimeFormatter by lazy {
        val pattern = DateFormat.getBestDateTimePattern(localeProvider.current(), "d MMMMM")
        DateTimeFormatter.ofPattern(pattern)
    }

    override val dateWithYearFormatter: DateTimeFormatter by lazy {
        val pattern = DateFormat.getBestDateTimePattern(localeProvider.current(), "d MMM y")
        DateTimeFormatter.ofPattern(pattern)
    }
}
