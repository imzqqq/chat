package com.imzqqq.app.features.autocomplete.group

import com.airbnb.epoxy.TypedEpoxyController
import com.imzqqq.app.features.autocomplete.AutocompleteClickListener
import com.imzqqq.app.features.autocomplete.autocompleteMatrixItem
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.session.group.model.GroupSummary
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class AutocompleteGroupController @Inject constructor() : TypedEpoxyController<List<GroupSummary>>() {

    var listener: AutocompleteClickListener<GroupSummary>? = null

    @Inject lateinit var avatarRenderer: AvatarRenderer

    override fun buildModels(data: List<GroupSummary>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        val host = this
        data.forEach { groupSummary ->
            autocompleteMatrixItem {
                id(groupSummary.groupId)
                matrixItem(groupSummary.toMatrixItem())
                avatarRenderer(host.avatarRenderer)
                clickListener { host.listener?.onItemClick(groupSummary) }
            }
        }
    }
}
