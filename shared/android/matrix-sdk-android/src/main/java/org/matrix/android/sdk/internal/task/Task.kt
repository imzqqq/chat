package org.matrix.android.sdk.internal.task

import kotlinx.coroutines.delay
import org.matrix.android.sdk.api.failure.Failure
import org.matrix.android.sdk.api.failure.shouldBeRetried
import timber.log.Timber

internal interface Task<PARAMS, RESULT> {

    suspend fun execute(params: PARAMS): RESULT

    suspend fun executeRetry(params: PARAMS, remainingRetry: Int): RESULT {
        return try {
            execute(params)
        } catch (failure: Throwable) {
            if (failure.shouldBeRetried() && remainingRetry > 0) {
                Timber.d(failure, "## TASK: Retriable error")
                if (failure is Failure.ServerError) {
                    val waitTime = failure.error.retryAfterMillis ?: 0L
                    Timber.d(failure, "## TASK: Quota wait time $waitTime")
                    delay(waitTime + 100)
                }
                return executeRetry(params, remainingRetry - 1)
            }
            throw failure
        }
    }
}
