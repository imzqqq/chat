package com.imzqqq.app.flow.components.compose.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup
import com.imzqqq.app.R
import com.imzqqq.app.flow.entity.Status

class ComposeOptionsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RadioGroup(context, attrs) {

    var listener: ComposeOptionsListener? = null

    init {
        inflate(context, R.layout.view_compose_options, this)

        setOnCheckedChangeListener { _, checkedId ->
            val visibility = when (checkedId) {
                R.id.publicRadioButton ->
                    Status.Visibility.PUBLIC
                R.id.unlistedRadioButton ->
                    Status.Visibility.UNLISTED
                R.id.privateRadioButton ->
                    Status.Visibility.PRIVATE
                R.id.directRadioButton ->
                    Status.Visibility.DIRECT
                else ->
                    Status.Visibility.PUBLIC
            }
            listener?.onVisibilityChanged(visibility)
        }
    }

    fun setStatusVisibility(visibility: Status.Visibility) {
        val selectedButton = when (visibility) {
            Status.Visibility.PUBLIC   ->
                R.id.publicRadioButton
            Status.Visibility.UNLISTED ->
                R.id.unlistedRadioButton
            Status.Visibility.PRIVATE  ->
                R.id.privateRadioButton
            Status.Visibility.DIRECT   ->
                R.id.directRadioButton
            else                       ->
                R.id.directRadioButton
        }

        check(selectedButton)
    }
}

interface ComposeOptionsListener {
    fun onVisibilityChanged(visibility: Status.Visibility)
}
