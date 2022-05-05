package com.imzqqq.app.features.roomdirectory

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.core.platform.ButtonStateView
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.util.MatrixItem

@EpoxyModelClass(layout = R.layout.item_public_room)
abstract class PublicRoomItem : VectorEpoxyModel<PublicRoomItem.Holder>() {

    @EpoxyAttribute
    lateinit var avatarRenderer: AvatarRenderer

    @EpoxyAttribute
    lateinit var matrixItem: MatrixItem

    @EpoxyAttribute
    var roomAlias: String? = null

    @EpoxyAttribute
    var roomTopic: String? = null

    @EpoxyAttribute
    var nbOfMembers: Int = 0

    @EpoxyAttribute
    var joinState: JoinState = JoinState.NOT_JOINED

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var globalListener: ClickListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var joinListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.rootView.onClick(globalListener)

        avatarRenderer.render(matrixItem, holder.avatarView)
        holder.nameView.text = matrixItem.displayName
        holder.aliasView.setTextOrHide(roomAlias)
        holder.topicView.setTextOrHide(roomTopic)
        // TODO Use formatter for big numbers?
        holder.counterView.text = nbOfMembers.toString()

        holder.buttonState.render(
                when (joinState) {
                    JoinState.NOT_JOINED    -> ButtonStateView.State.Button
                    JoinState.JOINING       -> ButtonStateView.State.Loading
                    JoinState.JOINED        -> ButtonStateView.State.Loaded
                    JoinState.JOINING_ERROR -> ButtonStateView.State.Error
                }
        )

        holder.buttonState.commonClicked = { joinListener?.invoke(it) }
    }

    class Holder : VectorEpoxyHolder() {
        val rootView by bind<ViewGroup>(R.id.itemPublicRoomLayout)

        val avatarView by bind<ImageView>(R.id.itemPublicRoomAvatar)
        val nameView by bind<TextView>(R.id.itemPublicRoomName)
        val aliasView by bind<TextView>(R.id.itemPublicRoomAlias)
        val topicView by bind<TextView>(R.id.itemPublicRoomTopic)
        val counterView by bind<TextView>(R.id.itemPublicRoomMembersCount)

        val buttonState by bind<ButtonStateView>(R.id.itemPublicRoomButtonState)
    }
}
