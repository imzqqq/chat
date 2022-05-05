package com.imzqqq.app.core.platform

import androidx.lifecycle.ViewModel
import com.imzqqq.app.core.utils.MutableDataSource
import com.imzqqq.app.core.utils.PublishDataSource

interface VectorSharedAction

/**
 * Parent class to handle navigation events, action events, or other any events
 */
open class VectorSharedActionViewModel<T : VectorSharedAction>(private val store: MutableDataSource<T> = PublishDataSource()) :
    ViewModel(), MutableDataSource<T> by store
