package com.imzqqq.app.features.crypto.recover

fun String.formatRecoveryKey(): String {
    return this.replace(" ", "")
            .chunked(16)
            .joinToString("\n") {
                it
                        .chunked(4)
                        .joinToString(" ")
            }
}
