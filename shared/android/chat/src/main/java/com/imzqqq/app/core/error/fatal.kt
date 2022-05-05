package com.imzqqq.app.core.error

import timber.log.Timber

/**
 * throw in debug, only log in production. As this method does not always throw, next statement should be a return
 */
fun fatalError(message: String, failFast: Boolean) {
    if (failFast) {
        error(message)
    } else {
        Timber.e(message)
    }
}
