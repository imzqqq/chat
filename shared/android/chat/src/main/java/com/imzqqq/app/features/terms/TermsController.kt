package com.imzqqq.app.features.terms

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Incomplete
import com.airbnb.mvrx.Success
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.errorWithRetryItem
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.features.discovery.settingsSectionTitleItem
import javax.inject.Inject

class TermsController @Inject constructor(
        private val errorFormatter: ErrorFormatter
) : TypedEpoxyController<ReviewTermsViewState>() {

    var description: String? = null
    var listener: Listener? = null

    override fun buildModels(data: ReviewTermsViewState?) {
        data ?: return
        val host = this

        when (data.termsList) {
            is Incomplete -> {
                loadingItem {
                    id("loading")
                }
            }
            is Fail       -> {
                errorWithRetryItem {
                    id("errorRetry")
                    text(host.errorFormatter.toHumanReadable(data.termsList.error))
                    listener { host.listener?.retry() }
                }
            }
            is Success    -> buildTerms(data.termsList.invoke())
        }
    }

    private fun buildTerms(termsList: List<Term>) {
        val host = this
        settingsSectionTitleItem {
            id("header")
            titleResId(R.string.widget_integration_review_terms)
        }
        termsList.forEach { term ->
            termItem {
                id(term.url)
                name(term.name)
                description(host.description)
                checked(term.accepted)

                clickListener  { host.listener?.review(term) }
                checkChangeListener { _, isChecked ->
                    host.listener?.setChecked(term, isChecked)
                }
            }
        }
    }

    interface Listener {
        fun retry()
        fun setChecked(term: Term, isChecked: Boolean)
        fun review(term: Term)
    }
}
