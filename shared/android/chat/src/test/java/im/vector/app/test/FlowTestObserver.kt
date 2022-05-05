package com.imzqqq.app.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.junit.Assert.assertEquals

fun <T> Flow<T>.test(scope: CoroutineScope): FlowTestObserver<T> {
    return FlowTestObserver(scope, this)
}

class FlowTestObserver<T>(
        scope: CoroutineScope,
        flow: Flow<T>
) {
    private val values = mutableListOf<T>()
    private val job: Job = flow
            .onEach {
                values.add(it)
            }.launchIn(scope)

    fun assertNoValues(): FlowTestObserver<T> {
        assertEquals(emptyList<T>(), this.values)
        return this
    }

    fun assertValues(vararg values: T): FlowTestObserver<T> {
        assertEquals(values.toList(), this.values)
        return this
    }

    fun finish() {
        job.cancel()
    }
}
