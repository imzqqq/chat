package com.imzqqq.app.core.date

import javax.inject.Inject

class DateFormatterProviders @Inject constructor(private val defaultDateFormatterProvider: DefaultDateFormatterProvider,
                                                 private val abbrevDateFormatterProvider: AbbrevDateFormatterProvider) {

    fun provide(abbrev: Boolean): DateFormatterProvider {
        return if (abbrev) {
            abbrevDateFormatterProvider
        } else {
            defaultDateFormatterProvider
        }
    }
}
