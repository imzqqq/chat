package com.imzqqq.app.features.form

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.request.RequestOptions
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.glide.GlideApp
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.util.MatrixItem

@EpoxyModelClass(layout = R.layout.item_editable_avatar)
abstract class FormEditableAvatarItem : EpoxyModelWithHolder<FormEditableAvatarItem.Holder>() {

    @EpoxyAttribute
    var avatarRenderer: AvatarRenderer? = null

    @EpoxyAttribute
    var matrixItem: MatrixItem? = null

    @EpoxyAttribute
    var enabled: Boolean = true

    @EpoxyAttribute
    var imageUri: Uri? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: ClickListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var deleteListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.imageContainer.onClick(clickListener?.takeIf { enabled })
        if (matrixItem != null) {
            avatarRenderer?.render(matrixItem!!, holder.image)
        } else {
            GlideApp.with(holder.image)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.image)
        }
        holder.delete.isVisible = enabled && (imageUri != null || matrixItem?.avatarUrl?.isNotEmpty() == true)
        holder.delete.onClick(deleteListener?.takeIf { enabled })
    }

    class Holder : VectorEpoxyHolder() {
        val imageContainer by bind<View>(R.id.itemEditableAvatarImageContainer)
        val image by bind<ImageView>(R.id.itemEditableAvatarImage)
        val delete by bind<View>(R.id.itemEditableAvatarDelete)
    }
}
