package com.imzqqq.app.features.settings.push

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.hilt.EntryPoints
import com.imzqqq.app.core.di.SingletonEntryPoint
import com.imzqqq.app.core.platform.EmptyAction
import com.imzqqq.app.core.platform.EmptyViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import org.matrix.android.sdk.api.pushrules.rest.PushRule

data class PushRulesViewState(
        val rules: List<PushRule> = emptyList()
) : MavericksState

class PushRulesViewModel(initialState: PushRulesViewState) :
    VectorViewModel<PushRulesViewState, EmptyAction, EmptyViewEvents>(initialState) {

    companion object : MavericksViewModelFactory<PushRulesViewModel, PushRulesViewState> {

        override fun initialState(viewModelContext: ViewModelContext): PushRulesViewState? {
            val session = EntryPoints.get(viewModelContext.app(), SingletonEntryPoint::class.java).activeSessionHolder().getActiveSession()
            val rules = session.getPushRules().getAllRules()
            return PushRulesViewState(rules)
        }
    }

    override fun handle(action: EmptyAction) {
        // No op
    }
}
