package com.imzqqq.app.features.debug.sas

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import me.gujun.android.span.image
import me.gujun.android.span.span
import org.matrix.android.sdk.api.session.crypto.verification.EmojiRepresentation

@EpoxyModelClass(layout = com.imzqqq.app.R.layout.item_sas_emoji)
abstract class SasEmojiItem : VectorEpoxyModel<SasEmojiItem.Holder>() {

    @EpoxyAttribute
    var index: Int = 0
    @EpoxyAttribute
    lateinit var emojiRepresentation: EmojiRepresentation

    @SuppressLint("SetTextI18n")
    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.indexView.text = "$index:"
        holder.emojiView.text = span {
            +emojiRepresentation.emoji
            emojiRepresentation.drawableRes?.let {
                image(ContextCompat.getDrawable(holder.view.context, it)!!)
            }
        }
        holder.textView.setText(emojiRepresentation.nameResId)
        holder.idView.text = holder.idView.resources.getResourceEntryName(emojiRepresentation.nameResId)
    }

    class Holder : VectorEpoxyHolder() {
        val indexView by bind<TextView>(com.imzqqq.app.R.id.sas_emoji_index)
        val emojiView by bind<TextView>(com.imzqqq.app.R.id.sas_emoji)
        val textView by bind<TextView>(com.imzqqq.app.R.id.sas_emoji_text)
        val idView by bind<TextView>(com.imzqqq.app.R.id.sas_emoji_text_id)
    }
}
