package com.imzqqq.app.features.poll.create

import com.imzqqq.app.core.platform.VectorViewEvents

sealed class CreatePollViewEvents : VectorViewEvents {
    object Success : CreatePollViewEvents()
    object EmptyQuestionError : CreatePollViewEvents()
    data class NotEnoughOptionsError(val requiredOptionsCount: Int) : CreatePollViewEvents()
}
