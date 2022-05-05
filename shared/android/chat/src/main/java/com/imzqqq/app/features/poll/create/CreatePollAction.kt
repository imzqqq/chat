package com.imzqqq.app.features.poll.create

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class CreatePollAction : VectorViewModelAction {
    data class OnQuestionChanged(val question: String) : CreatePollAction()
    data class OnOptionChanged(val index: Int, val option: String) : CreatePollAction()
    data class OnDeleteOption(val index: Int) : CreatePollAction()
    object OnAddOption : CreatePollAction()
    object OnCreatePoll : CreatePollAction()
}
