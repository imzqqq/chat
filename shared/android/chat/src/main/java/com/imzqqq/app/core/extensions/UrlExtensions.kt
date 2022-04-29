package com.imzqqq.app.core.extensions

/**
 * Ex: "https://matrix.org/" -> "chat.imzqqq.top"
 */
fun String?.toReducedUrl(): String {
    return (this ?: "")
            .substringAfter("://")
            .trim { it == '/' }
}
