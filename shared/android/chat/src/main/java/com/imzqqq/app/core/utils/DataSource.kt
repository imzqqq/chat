package com.imzqqq.app.core.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface DataSource<T> {
    fun stream(): Flow<T>
}

interface MutableDataSource<T> : DataSource<T> {
    fun post(value: T)
}

/**
 * This datasource emits the most recent value it has observed and all subsequent observed values to each subscriber.
 */
open class BehaviorDataSource<T>(private val defaultValue: T? = null) : MutableDataSource<T> {

    private val mutableFlow = MutableSharedFlow<T>(replay = 1)

    val currentValue: T?
        get() = mutableFlow.replayCache.firstOrNull()

    override fun stream(): Flow<T> {
        return mutableFlow
    }

    override fun post(value: T) {
        mutableFlow.tryEmit(value)
    }
}

/**
 * This datasource only emits all subsequent observed values to each subscriber.
 */
open class PublishDataSource<T> : MutableDataSource<T> {

    private val mutableFlow = MutableSharedFlow<T>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun stream(): Flow<T> {
        return mutableFlow
    }

    override fun post(value: T) {
        mutableFlow.tryEmit(value)
    }
}
