package com.imzqqq.app.core.utils

import java.net.URL

fun String.isValidUrl(): Boolean {
    return try {
        URL(this)
        true
    } catch (t: Throwable) {
        false
    }
}

/**
 * Ensure string starts with "http". If it is not the case, "https://" is added, only if the String is not empty
 */
fun String.ensureProtocol(): String {
    return when {
        isEmpty()           -> this
        !startsWith("http") -> "https://$this"
        else                -> this
    }
}

fun String.ensureTrailingSlash(): String {
    return when {
        isEmpty()      -> this
        !endsWith("/") -> "$this/"
        else           -> this
    }
}
