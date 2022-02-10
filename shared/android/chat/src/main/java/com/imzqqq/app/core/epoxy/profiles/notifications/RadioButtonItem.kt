package com.imzqqq.app.core.epoxy.profiles.notifications

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setAttributeTintedImageResource

@EpoxyModelClass(layout = R.layout.item_radio)
abstract class RadioButtonItem : VectorEpoxyModel<RadioButtonItem.Holder>() {

    @EpoxyAttribute
    var title: CharSequence? = null

    @StringRes
    @EpoxyAttribute
    var titleRes: Int? = null

    @EpoxyAttribute
    var selected = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var listener: ClickListener

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.view.onClick(listener)
        if (titleRes != null) {
            holder.titleText.setText(titleRes!!)
        } else {
            holder.titleText.text = title
        }

        if (selected) {
            holder.radioImage.setAttributeTintedImageResource(R.drawable.ic_radio_on, R.attr.colorPrimary)
            holder.radioImage.contentDescription = holder.view.context.getString(R.string.a11y_checked)
        } else {
            holder.radioImage.setImageDrawable(ContextCompat.getDrawable(holder.view.context, R.drawable.ic_radio_off))
            holder.radioImage.contentDescription = holder.view.context.getString(R.string.a11y_unchecked)
        }
    }

    class Holder : VectorEpoxyHolder() {
        val titleText by bind<TextView>(R.id.actionTitle)
        val radioImage by bind<ImageView>(R.id.radioIcon)
    }
}
