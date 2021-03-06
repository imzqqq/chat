package com.imzqqq.app.core.platform

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.databinding.ViewButtonStateBinding

class ButtonStateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle) {

    sealed class State {
        object Button : State()
        object Loading : State()
        object Loaded : State()
        object Error : State()
    }

    var commonClicked: ClickListener? = null
    var buttonClicked: ClickListener? = null
    var retryClicked: ClickListener? = null

    // Big or Flat button
    var button: Button

    private val views: ViewButtonStateBinding

    init {
        inflate(context, R.layout.view_button_state, this)
        views = ViewButtonStateBinding.bind(this)

        layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        views.buttonStateRetry.onClick {
            commonClicked?.invoke(it)
            retryClicked?.invoke(it)
        }

        // Read attributes
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ButtonStateView,
                0, 0)
                .apply {
                    try {
                        if (getBoolean(R.styleable.ButtonStateView_bsv_use_flat_button, true)) {
                            button = views.buttonStateButtonFlat
                            views.buttonStateButtonBig.isVisible = false
                        } else {
                            button = views.buttonStateButtonBig
                            views.buttonStateButtonFlat.isVisible = false
                        }

                        button.text = getString(R.styleable.ButtonStateView_bsv_button_text)
                        views.buttonStateLoaded.setImageDrawable(getDrawable(R.styleable.ButtonStateView_bsv_loaded_image_src))
                    } finally {
                        recycle()
                    }
                }

        button.onClick {
            commonClicked?.invoke(it)
            buttonClicked?.invoke(it)
        }
    }

    fun render(newState: State) {
        if (newState == State.Button) {
            button.isVisible = true
        } else {
            // We use isInvisible because we want to keep button space in the layout
            button.isInvisible = true
        }

        views.buttonStateLoading.isVisible = newState == State.Loading
        views.buttonStateLoaded.isVisible = newState == State.Loaded
        views.buttonStateRetry.isVisible = newState == State.Error
    }
}
