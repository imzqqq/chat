package com.imzqqq.app.features.userdirectory

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel

@EpoxyModelClass(layout = R.layout.item_user_list_header)
abstract class UserListHeaderItem : VectorEpoxyModel<UserListHeaderItem.Holder>() {

    @EpoxyAttribute var header: String = ""

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.headerTextView.text = header
    }

    class Holder : VectorEpoxyHolder() {
        val headerTextView by bind<TextView>(R.id.userListHeaderView)
    }
}
