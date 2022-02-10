package com.imzqqq.app.test

import kotlinx.coroutines.Dispatchers
import org.matrix.android.sdk.api.MatrixCoroutineDispatchers

internal val testCoroutineDispatchers = MatrixCoroutineDispatchers(
        io = Dispatchers.Main,
        computation = Dispatchers.Main,
        main = Dispatchers.Main,
        crypto = Dispatchers.Main,
        dmVerif = Dispatchers.Main
)
