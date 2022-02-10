package com.imzqqq.app.core.platform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

interface ViewModelTask<Params, Result> {
    operator fun invoke(
            scope: CoroutineScope,
            params: Params,
            onResult: (Result) -> Unit = {}
    ) {
        val backgroundJob = scope.async { execute(params) }
        scope.launch { onResult(backgroundJob.await()) }
    }

    suspend fun execute(params: Params): Result
}
