package com.imzqqq.app.features.home.room.detail.search

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class SearchViewEvents : VectorViewEvents {
    data class Failure(val throwable: Throwable) : SearchViewEvents()
}
