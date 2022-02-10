package com.imzqqq.app.flow.viewdata

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.core.text.parseAsHtml
import com.imzqqq.app.R
import com.imzqqq.app.flow.entity.Poll
import com.imzqqq.app.flow.entity.PollOption
import java.util.Date
import kotlin.math.roundToInt

data class PollViewData(
        val id: String,
        val expiresAt: Date?,
        val expired: Boolean,
        val multiple: Boolean,
        val votesCount: Int,
        val votersCount: Int?,
        val options: List<PollOptionViewData>,
        var voted: Boolean
)

data class PollOptionViewData(
    val title: String,
    var votesCount: Int,
    var selected: Boolean
)

fun calculatePercent(fraction: Int, totalVoters: Int?, totalVotes: Int): Int {
    return if (fraction == 0) {
        0
    } else {
        val total = totalVoters ?: totalVotes
        (fraction / total.toDouble() * 100).roundToInt()
    }
}

fun buildDescription(title: String, percent: Int, context: Context): Spanned {
    return SpannableStringBuilder(context.getString(R.string.poll_percent_format, percent).parseAsHtml())
        .append(" ")
        .append(title)
}

fun Poll?.toViewData(): PollViewData? {
    if (this == null) return null
    return PollViewData(
        id = id,
        expiresAt = expiresAt,
        expired = expired,
        multiple = multiple,
        votesCount = votesCount,
        votersCount = votersCount,
        options = options.map { it.toViewData() },
        voted = voted
    )
}

fun PollOption.toViewData(): PollOptionViewData {
    return PollOptionViewData(
        title = title,
        votesCount = votesCount,
        selected = false
    )
}
