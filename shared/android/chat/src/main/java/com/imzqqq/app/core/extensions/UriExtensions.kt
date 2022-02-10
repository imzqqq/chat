package com.imzqqq.app.core.extensions

import android.net.Uri

const val IGNORED_SCHEMA = "ignored"

fun Uri.isIgnored() = scheme == IGNORED_SCHEMA

fun createIgnoredUri(path: String): Uri = Uri.parse("$IGNORED_SCHEMA://$path")
