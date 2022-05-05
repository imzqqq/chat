package com.imzqqq.app.features.home

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class HomeActivityViewActions : VectorViewModelAction {
    object PushPromptHasBeenReviewed : HomeActivityViewActions()
}
