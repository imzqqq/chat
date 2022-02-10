package com.imzqqq.app.core.epoxy.profiles

import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.setTextOrHide

@EpoxyModelClass(layout = R.layout.item_profile_matrix_item)
abstract class ProfileMatrixItemWithPowerLevel : ProfileMatrixItem() {

    @EpoxyAttribute var powerLevelLabel: CharSequence? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.editableView.isVisible = false
        holder.powerLabel.setTextOrHide(powerLevelLabel)
    }
}
