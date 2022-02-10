package com.imzqqq.app.features.spaces

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick

@EpoxyModelClass(layout = R.layout.item_space_beta_header)
abstract class SpaceBetaHeaderItem : VectorEpoxyModel<SpaceBetaHeaderItem.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickAction: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.feedBackAction.onClick(clickAction)
    }

    class Holder : VectorEpoxyHolder() {
        val feedBackAction by bind<View>(R.id.spaceBetaFeedbackAction)
    }
}
