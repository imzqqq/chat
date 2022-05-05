package com.imzqqq.app.test

import com.airbnb.mvrx.MavericksState
import com.imzqqq.app.core.platform.VectorViewEvents
import com.imzqqq.app.core.platform.VectorViewModel
import com.imzqqq.app.core.platform.VectorViewModelAction
import kotlinx.coroutines.CoroutineScope
import org.amshove.kluent.shouldBeEqualTo

fun String.trimIndentOneLine() = trimIndent().replace("\n", "")

fun <S : MavericksState, VA : VectorViewModelAction, VE : VectorViewEvents> VectorViewModel<S, VA, VE>.test(coroutineScope: CoroutineScope): ViewModelTest<S, VE> {
    val state = { com.airbnb.mvrx.withState(this) { it } }
    val viewEvents = viewEvents.stream().test(coroutineScope)
    return ViewModelTest(state, viewEvents)
}

class ViewModelTest<S, VE>(
        val state: () -> S,
        val viewEvents: FlowTestObserver<VE>
) {

    fun assertEvents(vararg expected: VE): ViewModelTest<S, VE> {
        viewEvents.assertValues(*expected)
        return this
    }

    fun assertState(expected: S): ViewModelTest<S, VE> {
        state() shouldBeEqualTo expected
        return this
    }

    fun finish() {
        viewEvents.finish()
    }
}
