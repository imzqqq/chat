package org.matrix.android.sdk.api.logger

/**
 * Parent class for custom logger tags. Can be used with Timber :
 *
 * val loggerTag = LoggerTag("MyTag", LoggerTag.VOIP)
 * Timber.tag(loggerTag.value).v("My log message")
 */
open class LoggerTag(_value: String, parentTag: LoggerTag? = null) {

    object SYNC : LoggerTag("SYNC")
    object VOIP : LoggerTag("VOIP")

    val value: String = if (parentTag == null) {
        _value
    } else {
        "${parentTag.value}/$_value"
    }
}
