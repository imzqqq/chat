package org.matrix.android.sdk.api

import kotlinx.coroutines.CoroutineDispatcher

data class MatrixCoroutineDispatchers(
        val io: CoroutineDispatcher,
        val computation: CoroutineDispatcher,
        val main: CoroutineDispatcher,
        val crypto: CoroutineDispatcher,
        val dmVerif: CoroutineDispatcher
)
