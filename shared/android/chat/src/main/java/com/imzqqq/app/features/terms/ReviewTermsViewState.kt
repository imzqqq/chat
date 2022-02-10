package com.imzqqq.app.features.terms

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized

data class ReviewTermsViewState(
        val termsList: Async<List<Term>> = Uninitialized
) : MavericksState
