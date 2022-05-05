package com.imzqqq.app.core.utils

import org.apache.commons.codec.binary.Base32

fun String.toBase32String(padding: Boolean = true): String {
    val base32 = Base32().encodeAsString(toByteArray())
    return if (padding) {
        base32
    } else {
        base32.trimEnd('=')
    }
}
