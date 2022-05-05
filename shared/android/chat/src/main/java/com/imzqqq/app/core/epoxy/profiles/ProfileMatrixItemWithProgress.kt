package com.imzqqq.app.core.epoxy.profiles

import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R

@EpoxyModelClass(layout = R.layout.item_profile_matrix_item_progress)
abstract class ProfileMatrixItemWithProgress : BaseProfileMatrixItem<ProfileMatrixItemWithProgress.Holder>() {

    @EpoxyAttribute var inProgress: Boolean = true

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.progress.isVisible = inProgress
    }

    class Holder : ProfileMatrixItem.Holder() {
        val progress by bind<ProgressBar>(R.id.matrixItemProgress)
    }
}
