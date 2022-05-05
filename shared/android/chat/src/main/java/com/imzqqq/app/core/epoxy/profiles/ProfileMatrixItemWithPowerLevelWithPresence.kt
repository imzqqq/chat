package com.imzqqq.app.core.epoxy.profiles

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import org.matrix.android.sdk.api.session.presence.model.UserPresence

@EpoxyModelClass(layout = R.layout.item_profile_matrix_item)
abstract class ProfileMatrixItemWithPowerLevelWithPresence : ProfileMatrixItemWithPowerLevel() {

    @EpoxyAttribute var userPresence: UserPresence? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.presenceImageView.render(userPresence = userPresence)
    }
}
