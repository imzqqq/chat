package im.vector.lib.attachmentviewer

import android.view.View
import im.vector.lib.attachmentviewer.databinding.ItemAnimatedImageAttachmentBinding

class AnimatedImageViewHolder constructor(
    itemView: View
) : BaseViewHolder(itemView) {

    val views = ItemAnimatedImageAttachmentBinding.bind(itemView)

    internal val target = DefaultImageLoaderTarget(this, views.imageView)

    override fun onRecycled() {
        super.onRecycled()
        views.imageView.setImageDrawable(null)
    }
}
