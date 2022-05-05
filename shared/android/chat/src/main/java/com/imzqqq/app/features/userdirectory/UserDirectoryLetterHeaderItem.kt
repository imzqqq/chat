package com.imzqqq.app.features.userdirectory

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel

@EpoxyModelClass(layout = R.layout.item_user_directory_letter_header)
abstract class UserDirectoryLetterHeaderItem : VectorEpoxyModel<UserDirectoryLetterHeaderItem.Holder>() {

    @EpoxyAttribute var letter: String = ""

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.letterView.text = letter
    }

    class Holder : VectorEpoxyHolder() {
        val letterView by bind<TextView>(R.id.userDirectoryLetterView)
    }
}
