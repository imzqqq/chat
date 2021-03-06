package com.imzqqq.app.features.popup

import android.app.Activity
import android.view.View
import androidx.annotation.DrawableRes
import com.imzqqq.app.R
import com.imzqqq.app.core.glide.GlideApp
import com.imzqqq.app.databinding.AlerterVerificationLayoutBinding
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.util.MatrixItem

class VerificationVectorAlert(uid: String,
                              title: String,
                              override val description: String,
                              @DrawableRes override val iconId: Int?,
                              /**
                               * Alert are displayed by default, but let this lambda return false to prevent displaying
                               */
                              override val shouldBeDisplayedIn: ((Activity) -> Boolean) = { true }
) : DefaultVectorAlert(uid, title, description, iconId, shouldBeDisplayedIn) {
    override val layoutRes = R.layout.alerter_verification_layout

    class ViewBinder(private val matrixItem: MatrixItem?,
                     private val avatarRenderer: AvatarRenderer) : VectorAlert.ViewBinder {

        override fun bind(view: View) {
            val views = AlerterVerificationLayoutBinding.bind(view)
            matrixItem?.let { avatarRenderer.render(it, views.ivUserAvatar, GlideApp.with(view.context.applicationContext)) }
        }
    }
}
