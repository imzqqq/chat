package com.imzqqq.app.flow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.emoji.text.EmojiCompat
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.databinding.ItemPollBinding
import com.imzqqq.app.flow.entity.Emoji
import com.imzqqq.app.flow.util.BindingHolder
import com.imzqqq.app.flow.util.emojify
import com.imzqqq.app.flow.util.visible
import com.imzqqq.app.flow.viewdata.PollOptionViewData
import com.imzqqq.app.flow.viewdata.buildDescription
import com.imzqqq.app.flow.viewdata.calculatePercent

class PollAdapter : RecyclerView.Adapter<BindingHolder<ItemPollBinding>>() {

    private var pollOptions: List<PollOptionViewData> = emptyList()
    private var voteCount: Int = 0
    private var votersCount: Int? = null
    private var mode = RESULT
    private var emojis: List<Emoji> = emptyList()
    private var resultClickListener: View.OnClickListener? = null
    private var animateEmojis = false

    fun setup(
            options: List<PollOptionViewData>,
            voteCount: Int,
            votersCount: Int?,
            emojis: List<Emoji>,
            mode: Int,
            resultClickListener: View.OnClickListener?,
            animateEmojis: Boolean
    ) {
        this.pollOptions = options
        this.voteCount = voteCount
        this.votersCount = votersCount
        this.emojis = emojis
        this.mode = mode
        this.resultClickListener = resultClickListener
        this.animateEmojis = animateEmojis
        notifyDataSetChanged()
    }

    fun getSelected(): List<Int> {
        return pollOptions.filter { it.selected }
            .map { pollOptions.indexOf(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemPollBinding> {
        val binding = ItemPollBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun getItemCount() = pollOptions.size

    override fun onBindViewHolder(holder: BindingHolder<ItemPollBinding>, position: Int) {

        val option = pollOptions[position]

        val resultTextView = holder.binding.statusPollOptionResult
        val radioButton = holder.binding.statusPollRadioButton
        val checkBox = holder.binding.statusPollCheckbox

        resultTextView.visible(mode == RESULT)
        radioButton.visible(mode == SINGLE)
        checkBox.visible(mode == MULTIPLE)

        when (mode) {
            RESULT -> {
                val percent = calculatePercent(option.votesCount, votersCount, voteCount)
                val emojifiedPollOptionText = buildDescription(option.title, percent, resultTextView.context)
                    .emojify(emojis, resultTextView, animateEmojis)
                resultTextView.text = EmojiCompat.get().process(emojifiedPollOptionText)

                val level = percent * 100

                resultTextView.background.level = level
                resultTextView.setOnClickListener(resultClickListener)
            }
            SINGLE -> {
                val emojifiedPollOptionText = option.title.emojify(emojis, radioButton, animateEmojis)
                radioButton.text = EmojiCompat.get().process(emojifiedPollOptionText)
                radioButton.isChecked = option.selected
                radioButton.setOnClickListener {
                    pollOptions.forEachIndexed { index, pollOption ->
                        pollOption.selected = index == holder.bindingAdapterPosition
                        notifyItemChanged(index)
                    }
                }
            }
            MULTIPLE -> {
                val emojifiedPollOptionText = option.title.emojify(emojis, checkBox, animateEmojis)
                checkBox.text = EmojiCompat.get().process(emojifiedPollOptionText)
                checkBox.isChecked = option.selected
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    pollOptions[holder.bindingAdapterPosition].selected = isChecked
                }
            }
        }
    }

    companion object {
        const val RESULT = 0
        const val SINGLE = 1
        const val MULTIPLE = 2
    }
}
