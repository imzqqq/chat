package com.imzqqq.app.features.contactsbook

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.contacts.MappedContact
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.features.home.AvatarRenderer

@EpoxyModelClass(layout = R.layout.item_contact_main)
abstract class ContactItem : VectorEpoxyModel<ContactItem.Holder>() {

    @EpoxyAttribute lateinit var avatarRenderer: AvatarRenderer
    @EpoxyAttribute lateinit var mappedContact: MappedContact

    override fun bind(holder: Holder) {
        super.bind(holder)
        // If name is empty, use userId as name and force it being centered
        holder.nameView.text = mappedContact.displayName
        avatarRenderer.render(mappedContact, holder.avatarImageView)
    }

    class Holder : VectorEpoxyHolder() {
        val nameView by bind<TextView>(R.id.contactDisplayName)
        val avatarImageView by bind<ImageView>(R.id.contactAvatar)
    }
}
