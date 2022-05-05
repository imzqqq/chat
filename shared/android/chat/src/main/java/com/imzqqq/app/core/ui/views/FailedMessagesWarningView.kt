package com.imzqqq.app.core.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ViewFailedMessagesWarningBinding

class FailedMessagesWarningView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    interface Callback {
        fun onDeleteAllClicked()
        fun onRetryClicked()
    }

    var callback: Callback? = null

    private lateinit var views: ViewFailedMessagesWarningBinding

    init {
        setupViews()
    }

    private fun setupViews() {
        inflate(context, R.layout.view_failed_messages_warning, this)
        views = ViewFailedMessagesWarningBinding.bind(this)

        views.failedMessagesDeleteAllButton.setOnClickListener { callback?.onDeleteAllClicked() }
        views.failedMessagesRetryButton.setOnClickListener { callback?.onRetryClicked() }
    }
}
