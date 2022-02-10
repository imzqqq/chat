package com.imzqqq.app.features.poll.create

import android.view.Gravity
import android.view.inputmethod.EditorInfo
import com.airbnb.epoxy.EpoxyController
import com.imzqqq.app.R
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.core.ui.list.ItemStyle
import com.imzqqq.app.core.ui.list.genericButtonItem
import com.imzqqq.app.core.ui.list.genericItem
import com.imzqqq.app.features.form.formEditTextItem
import com.imzqqq.app.features.form.formEditTextWithDeleteItem
import javax.inject.Inject

class CreatePollController @Inject constructor(
        private val stringProvider: StringProvider,
        private val colorProvider: ColorProvider
) : EpoxyController() {

    private var state: CreatePollViewState? = null
    var callback: Callback? = null

    fun setData(state: CreatePollViewState) {
        this.state = state
        requestModelBuild()
    }

    override fun buildModels() {
        val currentState = state ?: return
        val host = this

        genericItem {
            id("question_title")
            style(ItemStyle.BIG_TEXT)
            title(host.stringProvider.getString(R.string.create_poll_question_title))
        }

        val questionImeAction = if (currentState.options.isEmpty()) EditorInfo.IME_ACTION_DONE else EditorInfo.IME_ACTION_NEXT

        formEditTextItem {
            id("question")
            value(currentState.question)
            hint(host.stringProvider.getString(R.string.create_poll_question_hint))
            singleLine(true)
            imeOptions(questionImeAction)
            maxLength(500)
            onTextChange {
                host.callback?.onQuestionChanged(it)
            }
        }

        genericItem {
            id("options_title")
            style(ItemStyle.BIG_TEXT)
            title(host.stringProvider.getString(R.string.create_poll_options_title))
        }

        currentState.options.forEachIndexed { index, option ->
            val imeOptions = if (index == currentState.options.size - 1) EditorInfo.IME_ACTION_DONE else EditorInfo.IME_ACTION_NEXT
            formEditTextWithDeleteItem {
                id("option_$index")
                value(option)
                hint(host.stringProvider.getString(R.string.create_poll_options_hint, (index + 1)))
                singleLine(true)
                imeOptions(imeOptions)
                onTextChange {
                    host.callback?.onOptionChanged(index, it)
                }
                onDeleteClicked {
                    host.callback?.onDeleteOption(index)
                }
            }
        }

        if (currentState.canAddMoreOptions) {
            genericButtonItem {
                id("add_option")
                text(host.stringProvider.getString(R.string.create_poll_add_option))
                textColor(host.colorProvider.getColor(R.color.palette_element_green))
                gravity(Gravity.START)
                bold(true)
                buttonClickAction {
                    host.callback?.onAddOption()
                }
            }
        }
    }

    interface Callback {
        fun onQuestionChanged(question: String)
        fun onOptionChanged(index: Int, option: String)
        fun onDeleteOption(index: Int)
        fun onAddOption()
    }
}
