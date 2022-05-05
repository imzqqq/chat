package com.imzqqq.app.features.home.room.list

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
import com.imzqqq.app.features.displayname.getBestName
import com.imzqqq.app.features.home.AvatarRenderer
import com.imzqqq.app.features.invite.InviteButtonStateBinder
import org.matrix.android.sdk.api.session.room.members.ChangeMembershipState
import org.matrix.android.sdk.api.util.MatrixItem

@EpoxyModelClass(layout = R.layout.item_room_invitation)
abstract class RoomInvitationItem : VectorEpoxyModel<RoomInvitationItem.Holder>() {

    @EpoxyAttribute lateinit var avatarRenderer: AvatarRenderer
    @EpoxyAttribute lateinit var matrixItem: MatrixItem
    @EpoxyAttribute var secondLine: CharSequence? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var listener: ClickListener? = null
    @EpoxyAttribute lateinit var changeMembershipState: ChangeMembershipState
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var acceptListener: ClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var rejectListener: ClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.rootView.onClick(listener)
        holder.acceptView.commonClicked = acceptListener
        holder.rejectView.commonClicked = rejectListener
        InviteButtonStateBinder.bind(holder.acceptView, holder.rejectView, changeMembershipState)
        holder.titleView.text = matrixItem.getBestName()
        holder.subtitleView.setTextOrHide(secondLine)
        avatarRenderer.render(matrixItem, holder.avatarImageView)
    }

    class Holder : VectorEpoxyHolder() {
        val titleView by bind<TextView>(R.id.roomInvitationNameView)
        val subtitleView by bind<TextView>(R.id.roomInvitationSubTitle)
        val acceptView by bind<ButtonStateView>(R.id.roomInvitationAccept)
        val rejectView by bind<ButtonStateView>(R.id.roomInvitationReject)
        val avatarImageView by bind<ImageView>(R.id.roomInvitationAvatarImageView)
        val rootView by bind<ViewGroup>(R.id.itemRoomInvitationLayout)
    }
}
