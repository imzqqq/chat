package com.imzqqq.app.core.epoxy.profiles

import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.features.displayname.getBestName
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.crypto.RoomEncryptionTrustLevel
import org.matrix.android.sdk.api.util.MatrixItem

abstract class BaseProfileMatrixItem<T : ProfileMatrixItem.Holder> : VectorEpoxyModel<T>() {
    @EpoxyAttribute lateinit var avatarRenderer: AvatarRenderer
    @EpoxyAttribute lateinit var matrixItem: MatrixItem
    @EpoxyAttribute var editable: Boolean = true

    @EpoxyAttribute
    var userEncryptionTrustLevel: RoomEncryptionTrustLevel? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: ClickListener? = null

    @CallSuper
    override fun bind(holder: T) {
        super.bind(holder)
        val bestName = matrixItem.getBestName()
        val matrixId = matrixItem.id
                .takeIf { it != bestName }
                // Special case for ThreePid fake matrix item
                .takeIf { it != "@" }
        holder.view.onClick(clickListener?.takeIf { editable })
        holder.titleView.text = bestName
        holder.subtitleView.setTextOrHide(matrixId)
        holder.editableView.isVisible = editable
        avatarRenderer.render(matrixItem, holder.avatarImageView)
        holder.avatarDecorationImageView.render(userEncryptionTrustLevel)
    }
}
