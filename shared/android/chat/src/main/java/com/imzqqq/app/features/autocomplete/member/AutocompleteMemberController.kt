package com.imzqqq.app.features.autocomplete.member

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.features.autocomplete.AutocompleteClickListener
import com.imzqqq.app.features.autocomplete.autocompleteMatrixItem
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.session.room.model.RoomMemberSummary
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class AutocompleteMemberController @Inject constructor() : TypedEpoxyController<List<RoomMemberSummary>>() {

    var listener: AutocompleteClickListener<RoomMemberSummary>? = null

    @Inject lateinit var avatarRenderer: AvatarRenderer

    override fun buildModels(data: List<RoomMemberSummary>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        val host = this
        data.forEach { user ->
            autocompleteMatrixItem {
                id(user.userId)
                matrixItem(user.toMatrixItem())
                avatarRenderer(host.avatarRenderer)
                clickListener { host.listener?.onItemClick(user) }
            }
        }
    }
}
