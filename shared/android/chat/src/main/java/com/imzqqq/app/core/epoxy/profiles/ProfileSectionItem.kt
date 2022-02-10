package com.imzqqq.app.core.epoxy.profiles

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel

@EpoxyModelClass(layout = R.layout.item_profile_section)
abstract class ProfileSectionItem : VectorEpoxyModel<ProfileSectionItem.Holder>() {

    @EpoxyAttribute
    lateinit var title: String

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.sectionView.text = title
    }

    class Holder : VectorEpoxyHolder() {
        val sectionView by bind<TextView>(R.id.itemProfileSectionView)
    }
}
