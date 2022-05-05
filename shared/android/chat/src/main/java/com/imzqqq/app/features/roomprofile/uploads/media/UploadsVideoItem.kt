package com.imzqqq.app.features.roomprofile.uploads.media

import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.features.media.ImageContentRenderer
import com.imzqqq.app.features.media.VideoContentRenderer

@EpoxyModelClass(layout = R.layout.item_uploads_video)
abstract class UploadsVideoItem : VectorEpoxyModel<UploadsVideoItem.Holder>() {

    @EpoxyAttribute lateinit var imageContentRenderer: ImageContentRenderer
    @EpoxyAttribute lateinit var data: VideoContentRenderer.Data
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var listener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.view.onClick(listener)
        imageContentRenderer.render(data.thumbnailMediaData, holder.imageView, IMAGE_SIZE_DP)
        ViewCompat.setTransitionName(holder.imageView, "videoPreview_${id()}")
    }

    class Holder : VectorEpoxyHolder() {
        val imageView by bind<ImageView>(R.id.uploadsVideoPreview)
    }
}
