package com.imzqqq.app.core.extensions

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success

/**
 * It maybe already exist somewhere but I cannot find it
 */
suspend fun <T> tryAsync(block: suspend () -> T): Async<T> {
    return try {
        Success(block.invoke())
    } catch (failure: Throwable) {
        Fail(failure)
    }
}
