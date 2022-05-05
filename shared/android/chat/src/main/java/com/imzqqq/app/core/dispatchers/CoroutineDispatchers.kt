package com.imzqqq.app.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

data class CoroutineDispatchers @Inject constructor(
        val io: CoroutineDispatcher,
        val computation: CoroutineDispatcher)
