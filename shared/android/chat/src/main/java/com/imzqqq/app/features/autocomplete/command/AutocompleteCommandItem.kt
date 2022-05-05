package com.imzqqq.app.features.autocomplete.command

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick

@EpoxyModelClass(layout = R.layout.item_autocomplete_command)
abstract class AutocompleteCommandItem : VectorEpoxyModel<AutocompleteCommandItem.Holder>() {

    @EpoxyAttribute
    var name: CharSequence? = null

    @EpoxyAttribute
    var parameters: CharSequence? = null

    @EpoxyAttribute
    var description: CharSequence? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.view.onClick(clickListener)
        holder.nameView.text = name
        holder.parametersView.text = parameters
        holder.descriptionView.text = description
    }

    class Holder : VectorEpoxyHolder() {
        val nameView by bind<TextView>(R.id.commandName)
        val parametersView by bind<TextView>(R.id.commandParameter)
        val descriptionView by bind<TextView>(R.id.commandDescription)
    }
}
