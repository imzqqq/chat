package com.imzqqq.app.features.terms

import com.imzqqq.app.core.platform.VectorViewModelAction

sealed class ReviewTermsAction : VectorViewModelAction {
    data class LoadTerms(val preferredLanguageCode: String) : ReviewTermsAction()
    data class MarkTermAsAccepted(val url: String, val accepted: Boolean) : ReviewTermsAction()
    object Accept : ReviewTermsAction()
}
