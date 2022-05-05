package com.imzqqq.app.features.home

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class HomeDetailAction : VectorViewModelAction {
    data class SwitchTab(val tab: HomeTab) : HomeDetailAction()
    object MarkAllRoomsRead : HomeDetailAction()
    data class StartCallWithPhoneNumber(val phoneNumber: String) : HomeDetailAction()
}
