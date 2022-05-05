package com.imzqqq.app.core.utils

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Use this container to read a value only once
 */
class ReadOnce<T>(
        private val value: T
) {
    private val valueHasBeenRead = AtomicBoolean(false)

    fun get(): T? {
        return if (valueHasBeenRead.getAndSet(true)) {
            null
        } else {
            value
        }
    }
}

/**
 * Only the first call to isTrue() will return true
 */
class ReadOnceTrue {
    private val readOnce = ReadOnce(true)

    fun isTrue() = readOnce.get() == true
}
