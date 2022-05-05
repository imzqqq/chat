package com.imzqqq.app.features.home

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class HomeDetailViewEvents : VectorViewEvents {
    object Loading : HomeDetailViewEvents()
    object CallStarted : HomeDetailViewEvents()
    data class FailToCall(val failure: Throwable) : HomeDetailViewEvents()
}
