package com.imzqqq.app.core.extensions

inline fun <reified T> List<T>.nextOrNull(index: Int) = getOrNull(index + 1)
inline fun <reified T> List<T>.prevOrNull(index: Int) = getOrNull(index - 1)
