package com.imzqqq.app.features.call.conference

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class JitsiCallViewActions : VectorViewModelAction {
    data class SwitchTo(val args: VectorJitsiActivity.Args,
                        val withConfirmation: Boolean) : JitsiCallViewActions()

    /**
     * The ViewModel will either ask the View to finish, or to join another conf.
     */
    object OnConferenceLeft : JitsiCallViewActions()
}
