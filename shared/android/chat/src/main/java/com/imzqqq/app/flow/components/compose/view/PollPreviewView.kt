package com.imzqqq.app.flow.components.compose.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.imzqqq.app.R
import com.imzqqq.app.flow.adapter.PreviewPollOptionsAdapter
import com.imzqqq.app.databinding.ViewPollPreviewBinding
import com.imzqqq.app.flow.entity.NewPoll

class PollPreviewView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    private val adapter = PreviewPollOptionsAdapter()

    private val binding = ViewPollPreviewBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL

        setBackgroundResource(R.drawable.card_frame)

        val padding = resources.getDimensionPixelSize(R.dimen.poll_preview_padding)

        setPadding(padding, padding, padding, padding)

        binding.pollPreviewOptions.adapter = adapter
    }

    fun setPoll(poll: NewPoll) {
        adapter.update(poll.options, poll.multiple)

        val pollDurationId = resources.getIntArray(R.array.poll_duration_values).indexOfLast {
            it <= poll.expiresIn
        }
        binding.pollDurationPreview.text = resources.getStringArray(R.array.poll_duration_names)[pollDurationId]
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        adapter.setOnClickListener(l)
    }
}
